package com.nicofercavv.catalog_service.domain.model;

import lombok.Data;

@Data
public class SneakerVariation {
    // Ex: "NIKE-AJ1-BRED-40", "NIKE-AJ1-BRED-41"
    private String sku;

    // Ex: "40", "41", "42"
    private String size;

    // Flag individual: se este tamanho específico acabar, o botão fica cinza no front
    private boolean inStock;
}
