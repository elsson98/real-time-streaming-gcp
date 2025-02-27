package com.elson.etl.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum InventoryReason {
    RESTOCK,
    SALE,
    RETURN,
    DAMAGE;

    @JsonCreator
    public static InventoryReason forValue(String value) {
        return InventoryReason.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name().toLowerCase();
    }
}