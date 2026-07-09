package com.nicofercavv.inventory_service.infrastructure.messaging;

import com.nicofercavv.inventory_service.infrastructure.messaging.dto.InventoryUpdatedEvent;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class InventoryEventPublisher {

    private final StreamBridge streamBridge;

    public InventoryEventPublisher(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void publishInventoryUpdated(InventoryUpdatedEvent event) {
        System.out.println("Disparando atualização de estoque para o Kafka. SKU: " + event.sku());
        streamBridge.send("inventoryUpdated-out-0", event);
    }
}
