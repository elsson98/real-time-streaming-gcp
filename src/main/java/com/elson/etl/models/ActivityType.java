package com.elson.etl.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ActivityType {
    LOGIN,
    LOGOUT,
    VIEW_PRODUCT,
    ADD_TO_CART,
    REMOVE_FROM_CART;

    @JsonCreator
    public static ActivityType forValue(String value) {
        return ActivityType.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name().toLowerCase();
    }
}