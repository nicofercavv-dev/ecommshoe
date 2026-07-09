package com.nicofercavv.catalog_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "sneakers")
public class Sneaker {
    @Id
    private String id;
    private String name;
    private String brand;
    private String description;
    private Long priceInCents;
    private List<String> imageUrls;
    private boolean isAvailable;

    private List<SneakerVariation> variations;

    // Campo onde o MongoDB guardará o array de números (vetor) gerado pela IA
    private List<Double> sneakerEmbedding;
}
