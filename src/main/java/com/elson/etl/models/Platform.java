package com.elson.etl.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Platform {
    WEB,
    MOBILE,
    TABLET;

    @JsonCreator
    public static Platform forValue(String value) {
        return Platform.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name().toLowerCase();
    }
}