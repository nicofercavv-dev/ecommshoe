package com.nicofercavv.catalog_service.infrastructure.messaging;

import com.nicofercavv.catalog_service.infrastructure.messaging.dto.ProductCreatedEvent;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class ProductEventPublisher {

    private final StreamBridge streamBridge;

    public ProductEventPublisher(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void publishProductCreated(ProductCreatedEvent event) {
        System.out.println("Publicando evento de produto criado para o Kafka: " + event.productId());

        // O primeiro parâmetro é o nome do binding configurado no application.yml
        streamBridge.send("productCreated-out-0", event);
    }
}
