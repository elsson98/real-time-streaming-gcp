package com.elson.etl.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.services.bigquery.model.TableRow;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingAddress implements Serializable {
    private static final long serialVersionUID = -4228362611378888727L;

    @JsonProperty("street")
    private String street;

    @JsonProperty("city")
    private String city;

    @JsonProperty("country")
    private String country;

    public TableRow toTableRow() {
        TableRow row = new TableRow();
        row.set("street", street);
        row.set("city", city);
        row.set("country", country);
        return row;
    }
}