package com.nicofercavv.catalog_service.infrastructure.repository;

import com.nicofercavv.catalog_service.domain.model.Sneaker;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;

import java.util.List;

public class CustomProductRepositoryImpl implements CustomProductRepository {

    private final MongoTemplate mongoTemplate;

    public CustomProductRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Sneaker> vectorSearch(List<Double> embedding, int maxResults) {
        // Constrói o estágio de busca vetorial do MongoDB Atlas / Enterprise
        Document vectorSearchDoc = new Document("$vectorSearch", new Document()
                .append("index", "vector_index") // Nome do índice vetorial criado no MongoDB
                .append("path", "sneakerEmbedding") // O campo na entidade Sneaker
                .append("queryVector", embedding)
                .append("numCandidates", maxResults * 10) // Quantidade de candidatos a analisar
                .append("limit", maxResults));

        // Transforma o Document b0s em um estágio de agregação do Spring Data Mongo
        Aggregation aggregation = Aggregation.newAggregation(
                context -> vectorSearchDoc
        );

        return mongoTemplate.aggregate(aggregation, "sneakers", Sneaker.class).getMappedResults();
    }
}
