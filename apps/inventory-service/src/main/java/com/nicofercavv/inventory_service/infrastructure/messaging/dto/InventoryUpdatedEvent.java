package com.nicofercavv.inventory_service.infrastructure.messaging.dto;

public record InventoryUpdatedEvent(
        String sku,
        int availableQuantity,
        boolean inStock
) {}
