package com.nicofercavv.inventory_service.infrastructure.messaging.dto;

import java.util.List;

public record ProductCreatedEvent(
        String productId,
        List<String> skus
) {}
