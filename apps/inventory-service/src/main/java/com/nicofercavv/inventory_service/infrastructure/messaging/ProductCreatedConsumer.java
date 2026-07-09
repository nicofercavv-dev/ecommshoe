package com.nicofercavv.inventory_service.infrastructure.messaging;

import com.nicofercavv.inventory_service.infrastructure.entity.InventoryEntity;
import com.nicofercavv.inventory_service.infrastructure.messaging.dto.ProductCreatedEvent;
import com.nicofercavv.inventory_service.infrastructure.repository.InventoryRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.function.Consumer;

@Component
public class ProductCreatedConsumer {

    private final InventoryRepository repository;

    public ProductCreatedConsumer(InventoryRepository repository) {
        this.repository = repository;
    }

    @Bean
    public Consumer<ProductCreatedEvent> productCreated() {
        return event -> {
            System.out.println("Recebido evento do Catálogo para o Produto: " + event.productId());

            // Para cada SKU criado no catálogo, inicializa a linha no banco do inventário
            event.skus().forEach(sku -> {
                if (repository.findBySkuCode(sku).isEmpty()) {
                    var initialInventory = InventoryEntity.builder()
                            .skuCode(sku)
                            .quantity(0) // Começa zerado até o admin abastecer
                            .reservedQuantity(0)
                            .updatedAt(LocalDateTime.now())
                            .build();
                    repository.save(initialInventory);
                    System.out.println("SKU cadastrado no inventário: " + sku);
                }
            });
        };
    }
}
