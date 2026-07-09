package com.nicofercavv.catalog_service.infrastructure.messaging;

import com.nicofercavv.catalog_service.domain.model.Sneaker;
import com.nicofercavv.catalog_service.domain.model.SneakerVariation;
import com.nicofercavv.catalog_service.infrastructure.messaging.dto.InventoryUpdatedEvent;
import com.nicofercavv.catalog_service.infrastructure.repository.MongoProductRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class InventoryUpdatedConsumer {

    private final MongoTemplate mongoTemplate;
    private final MongoProductRepository repository;

    public InventoryUpdatedConsumer(MongoTemplate mongoTemplate, MongoProductRepository repository) {
        this.mongoTemplate = mongoTemplate;
        this.repository = repository;
    }

    @Bean
    public Consumer<InventoryUpdatedEvent> inventoryUpdated() {
        return event -> {
            System.out.println("Catálogo processando mudança de estoque para o SKU: " + event.sku());

            // 1. Atualiza atomicamente a variação dentro da lista no MongoDB
            Query query = new Query(Criteria.where("variations.sku").is(event.sku()));
            Update update = new Update().set("variations.$.inStock", event.inStock());
            mongoTemplate.updateFirst(query, update, Sneaker.class);

            // 2. Busca o documento completo para recalcular se o tênis como um todo está disponível
            Sneaker sneaker = mongoTemplate.findOne(query, Sneaker.class);
            if (sneaker != null) {
                boolean anyInStock = sneaker.getVariations().stream().anyMatch(SneakerVariation::isInStock);
                sneaker.setAvailable(anyInStock);
                repository.save(sneaker);
                System.out.println("Disponibilidade do produto " + sneaker.getName() + " atualizada para: " + anyInStock);
            }
        };
    }
}
