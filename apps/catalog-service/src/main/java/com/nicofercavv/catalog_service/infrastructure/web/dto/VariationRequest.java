package com.nicofercavv.catalog_service.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;

public record VariationRequest(
        @NotBlank String sku,
        @NotBlank String size
) {}
