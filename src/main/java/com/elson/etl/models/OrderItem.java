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
public class OrderItem implements Serializable {
    private static final long serialVersionUID = -4228362611378888727L;

    @JsonProperty("product_id")
    private String productId;

    @JsonProperty("product_name")
    private String productName;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("price")
    private float price;

    public TableRow toTableRow() {
        TableRow row = new TableRow();
        row.set("product_id", productId);
        row.set("product_name", productName);
        row.set("quantity", quantity);
        row.set("price", price);
        return row;
    }
}