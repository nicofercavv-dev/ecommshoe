package com.nicofercavv.catalog_service.infrastructure.messaging.dto;

public record InventoryUpdatedEvent(String sku, int availableQuantity, boolean inStock) {}
