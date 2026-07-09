package com.nicofercavv.catalog_service.domain.usecase;

import com.nicofercavv.catalog_service.domain.model.Sneaker;
import com.nicofercavv.catalog_service.infrastructure.ai.OllamaEmbeddingAdapter;
import com.nicofercavv.catalog_service.infrastructure.repository.CustomProductRepository;
import com.nicofercavv.catalog_service.infrastructure.repository.MongoProductRepository;

import java.util.List;

public class SearchProductsByImageUseCase {

    private final CustomProductRepository repository;
    private final OllamaEmbeddingAdapter ollamaEmbeddingAdapter;

    public SearchProductsByImageUseCase(CustomProductRepository repository, OllamaEmbeddingAdapter ollamaEmbeddingAdapter) {
        this.repository = repository;
        this.ollamaEmbeddingAdapter = ollamaEmbeddingAdapter;
    }

    public List<Sneaker> execute(byte[] imageBytes) {
        if (imageBytes == null || imageBytes.length == 0) {
            throw new IllegalArgumentException("A imagem não pode estar vazia.");
        }

        // 1. Gera o embedding a partir dos bytes da imagem enviada no POST
        List<Double> queryEmbedding = ollamaEmbeddingAdapter.generateEmbeddingFromBytes(imageBytes);

        // 2. Busca no MongoDB usando busca vetorial (limite de 5 resultados mais similares)
        return repository.vectorSearch(queryEmbedding, 5);
    }
}
