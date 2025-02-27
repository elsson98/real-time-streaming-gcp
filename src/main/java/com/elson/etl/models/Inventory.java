package com.elson.etl.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.services.bigquery.model.TableRow;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.Instant;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inventory implements Serializable {
    private static final long serialVersionUID = -4228362611378888727L;

    @JsonProperty("event_type")
    private String eventType;

    @JsonProperty("inventory_id")
    private String inventoryId;

    @JsonProperty("product_id")
    private String productId;

    @JsonProperty("warehouse_id")
    private String warehouseId;

    @JsonProperty("quantity_change")
    private int quantityChange;

    @JsonProperty("reason")
    private InventoryReason reason;

    @JsonProperty("timestamp")
    private String timestamp;

    private Instant ingestionTime;

    public TableRow toTableRow() {
        TableRow row = new TableRow();
        row.set("event_type", eventType);
        row.set("inventory_id", inventoryId);
        row.set("product_id", productId);
        row.set("warehouse_id", warehouseId);
        row.set("quantity_change", quantityChange);
        row.set("reason", reason != null ? reason.toValue() : null);
        row.set("timestamp", timestamp);
        if (ingestionTime != null) {
            row.set("ingestion_time", ingestionTime.toString());
        }
        return row;
    }
}