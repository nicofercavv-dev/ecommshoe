package com.nicofercavv.catalog_service.domain.usecase;

import com.nicofercavv.catalog_service.domain.model.Sneaker;
import com.nicofercavv.catalog_service.domain.model.SneakerVariation;
import com.nicofercavv.catalog_service.infrastructure.ai.OllamaEmbeddingAdapter;
import com.nicofercavv.catalog_service.infrastructure.messaging.ProductEventPublisher;
import com.nicofercavv.catalog_service.infrastructure.messaging.dto.ProductCreatedEvent;
import com.nicofercavv.catalog_service.infrastructure.repository.MongoProductRepository;
import com.nicofercavv.catalog_service.infrastructure.web.dto.CreateProductRequest;

import java.util.List;
import java.util.stream.Collectors;

public class CreateProductUseCase {

    private final MongoProductRepository repository;
    private final ProductEventPublisher eventPublisher;
    private final OllamaEmbeddingAdapter ollamaEmbeddingAdapter;

    public CreateProductUseCase(MongoProductRepository repository, ProductEventPublisher eventPublisher, OllamaEmbeddingAdapter ollamaEmbeddingAdapter) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
        this.ollamaEmbeddingAdapter = ollamaEmbeddingAdapter;
    }

    public Sneaker execute(Sneaker sneaker) {

        if (sneaker.getImageUrls() != null && !sneaker.getImageUrls().isEmpty()) {
            try {
                String primaryImageUrl = sneaker.getImageUrls().get(0);
                List<Double> embedding = ollamaEmbeddingAdapter.generateEmbeddingFromUrl(primaryImageUrl);
                sneaker.setSneakerEmbedding(embedding);
            } catch (Exception e) {
                System.err.println("Erro ao gerar embedding da imagem: " + e.getMessage());
            }
        }

        Sneaker saved = repository.save(sneaker);

        List<String> skusCreated = saved.getVariations().stream()
                .map(SneakerVariation::getSku)
                .collect(Collectors.toList());

        eventPublisher.publishProductCreated(new ProductCreatedEvent(saved.getId(), skusCreated));

        return saved;
    }
}
