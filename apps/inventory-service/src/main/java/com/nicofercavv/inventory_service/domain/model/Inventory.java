package com.nicofercavv.inventory_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Inventory {
    private final String skuCode;
    private int quantity;
    private int reservedQuantity;

    public boolean hasAvailableStock(int requestedQuantity) {
        return (this.quantity - this.reservedQuantity) >= requestedQuantity;
    }

    public void reserve(int requestedQuantity) {
        if (!hasAvailableStock(requestedQuantity)) {
            throw new IllegalStateException("Estoque insuficiente para o SKU: " + skuCode);
        }
        this.reservedQuantity += requestedQuantity;
    }

    public void confirmSale(int requestedQuantity) {
        if (this.reservedQuantity < requestedQuantity) {
            throw new IllegalStateException("Quantidade reservada inconsistente para o SKU: " + skuCode);
        }
        this.quantity -= requestedQuantity;
        this.reservedQuantity -= requestedQuantity;
    }

    public void cancelReservation(int requestedQuantity) {
        if (this.reservedQuantity < requestedQuantity) {
            throw new IllegalStateException("Não há reservas suficientes para cancelar no SKU: " + skuCode);
        }
        this.reservedQuantity -= requestedQuantity;
    }
}
