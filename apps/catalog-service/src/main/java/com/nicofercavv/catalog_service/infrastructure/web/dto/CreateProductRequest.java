package com.nicofercavv.catalog_service.infrastructure.web.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record CreateProductRequest(
        @NotBlank(message = "O nome é obrigatório") String name,
        @NotBlank(message = "A marca é obrigatória") String brand,
        String description,
        @Min(value = 1, message = "O preço deve ser maior que zero") Long priceInCents,
        @NotEmpty(message = "O produto precisa de pelo menos uma imagem") List<String> imageUrls,
        List<VariationRequest> variations
) {}
