package com.nicofercavv.inventory_service.infrastructure.web;

import com.nicofercavv.inventory_service.infrastructure.entity.InventoryEntity;
import com.nicofercavv.inventory_service.infrastructure.messaging.InventoryEventPublisher;
import com.nicofercavv.inventory_service.infrastructure.messaging.dto.InventoryUpdatedEvent;
import com.nicofercavv.inventory_service.infrastructure.repository.InventoryRepository;
import com.nicofercavv.inventory_service.infrastructure.web.dto.UpdateStockRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryRepository repository;
    private final InventoryEventPublisher eventPublisher;

    public InventoryController(InventoryRepository repository, InventoryEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    // 1. Consultar o estoque real de um SKU específico
    @GetMapping("/{skuCode}")
    public ResponseEntity<InventoryEntity> getStockBySku(@PathVariable String skuCode) {
        return repository.findBySkuCode(skuCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 2. Atualizar a quantidade física de estoque (Abastecimento do Galpão)
    @PutMapping("/{skuCode}")
    @Transactional
    public ResponseEntity<InventoryEntity> updateStock(
            @PathVariable String skuCode,
            @Valid @RequestBody UpdateStockRequest request) {

        var entity = repository.findBySkuCodeForUpdate(skuCode)
                .orElseThrow(() -> new IllegalArgumentException("SKU não encontrado: " + skuCode));

        // Atualiza a quantidade física real
        entity.setQuantity(request.quantity());
        entity.setUpdatedAt(LocalDateTime.now());
        var updatedEntity = repository.save(entity);

        int realAvailable = updatedEntity.getQuantity() - updatedEntity.getReservedQuantity();
        boolean inStock = realAvailable > 0;

        eventPublisher.publishInventoryUpdated(new InventoryUpdatedEvent(skuCode, realAvailable, inStock));

        return ResponseEntity.ok(updatedEntity);
    }
}
