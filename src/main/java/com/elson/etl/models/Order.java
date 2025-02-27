package com.elson.etl.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.services.bigquery.model.TableRow;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.avro.reflect.Nullable;
import org.joda.time.Instant;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order implements Serializable {
    private static final long serialVersionUID = -4228362611378888727L;

    @JsonProperty("event_type")
    private String eventType;

    @JsonProperty("order_id")
    private String orderId;

    @JsonProperty("customer_id")
    private String customerId;

    @JsonProperty("order_date")
    private String orderDate;

    @JsonProperty("status")
    private OrderStatus status;

    @JsonProperty("items")
    private List<OrderItem> items;

    @Nullable
    @JsonProperty("shipping_address")
    private ShippingAddress shippingAddress;

    @JsonProperty("total_amount")
    private float totalAmount;

    private Instant ingestionTime;

    public TableRow toTableRow() {
        TableRow row = new TableRow();
        row.set("event_type", eventType);
        row.set("order_id", orderId);
        row.set("customer_id", customerId);
        row.set("order_date", orderDate);
        row.set("status", status);
        if (items != null) {
            row.set("items", items.stream().map(OrderItem::toTableRow).collect(Collectors.toList()));
        }
        if (shippingAddress != null) {
            row.set("shipping_address", shippingAddress.toTableRow());
        }
        row.set("total_amount", totalAmount);
        if (ingestionTime != null) {
            row.set("ingestion_time", ingestionTime.toString());
        }
        return row;
    }
}