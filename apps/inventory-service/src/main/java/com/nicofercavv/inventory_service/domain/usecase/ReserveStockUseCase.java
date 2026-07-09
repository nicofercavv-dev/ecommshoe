package com.nicofercavv.inventory_service.domain.usecase;

import com.nicofercavv.inventory_service.domain.model.Inventory;
import com.nicofercavv.inventory_service.infrastructure.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ReserveStockUseCase {

    private final InventoryRepository repository;

    public ReserveStockUseCase(InventoryRepository repository) {
        this.repository = repository;
    }

    @Transactional // Garante o rollback automático se algo falhar
    public void execute(String skuCode, int quantity) {
        // 1. Busca aplicando o Lock Pessimista no Postgres
        var entity = repository.findBySkuCodeForUpdate(skuCode)
                .orElseThrow(() -> new IllegalArgumentException("SKU não encontrado no inventário: " + skuCode));

        // 2. Transforma em modelo de domínio puro
        var inventory = new Inventory(entity.getSkuCode(), entity.getQuantity(), entity.getReservedQuantity());

        // 3. Executa a regra de negócio de reserva
        inventory.reserve(quantity);

        // 4. Atualiza a entidade de infraestrutura e salva
        entity.setReservedQuantity(inventory.getReservedQuantity());
        entity.setUpdatedAt(LocalDateTime.now());
        repository.save(entity);

        // TODO: Aqui dispararemos o evento de "Estoque Reservado com Sucesso" via Kafka futuramente
    }
}