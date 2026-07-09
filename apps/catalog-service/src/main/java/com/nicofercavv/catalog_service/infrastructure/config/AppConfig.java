package com.nicofercavv.catalog_service.infrastructure.config;

import com.nicofercavv.catalog_service.domain.usecase.CreateProductUseCase;
import com.nicofercavv.catalog_service.domain.usecase.SearchProductsByImageUseCase;
import com.nicofercavv.catalog_service.infrastructure.ai.OllamaEmbeddingAdapter;
import com.nicofercavv.catalog_service.infrastructure.messaging.ProductEventPublisher;
import com.nicofercavv.catalog_service.infrastructure.repository.MongoProductRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public SearchProductsByImageUseCase searchProductsByImageUseCase(
            MongoProductRepository repository,
            OllamaEmbeddingAdapter ollamaEmbeddingAdapter) {

        return new SearchProductsByImageUseCase(repository, ollamaEmbeddingAdapter);
    }

    @Bean
    public CreateProductUseCase createProductUseCase(
            MongoProductRepository repository, ProductEventPublisher eventPublisher, OllamaEmbeddingAdapter ollamaEmbeddingAdapter) {

        return new CreateProductUseCase(repository, eventPublisher, ollamaEmbeddingAdapter);
    }
}
