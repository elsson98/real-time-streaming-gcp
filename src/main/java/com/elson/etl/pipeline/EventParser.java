package com.elson.etl.pipeline;

import com.elson.etl.models.ErrorRecord;
import com.elson.etl.models.Inventory;
import com.elson.etl.models.Order;
import com.elson.etl.models.UserActivity;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.beam.sdk.transforms.DoFn;
import org.joda.time.Instant;

import java.util.Iterator;

import static com.elson.etl.pipeline.EventPipelineLocal.*;

public class EventParser extends DoFn<String, Order> {
    private transient ObjectMapper mapper;

    @Setup
    public void setup() {
        mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());
    }

    @ProcessElement
    public void processElement(ProcessContext c) {
        String json = null;
        try {
            json = c.element();
            JsonNode root = mapper.readTree(json);
            Iterator<String> fieldNames = root.fieldNames();

            String eventType = fieldNames.next();
            if (fieldNames.hasNext())
                throw new IllegalArgumentException("Invalid event format");

            JsonNode payload = root.get(eventType);

            switch (eventType) {
                case "OrderEvent":
                    Order order = mapper.treeToValue(payload, Order.class);
                    if (order.getIngestionTime() == null) order.setIngestionTime(new Instant());
                    c.output(ORDER_TAG, order);
                    break;
                case "InventoryEvent":
                    Inventory inventory = mapper.treeToValue(payload, Inventory.class);
                    int quantity = inventory.getQuantityChange();
                    if (quantity < -100 || quantity > 100) {
                        throw new IllegalArgumentException("Invalid quantityChange: must be between -100 and 100");
                    }
                    if (inventory.getIngestionTime() == null) {
                        inventory.setIngestionTime(new Instant());
                    }
                    c.output(INVENTORY_TAG, inventory);
                    break;
                case "UserActivityEvent":
                    UserActivity activity = mapper.treeToValue(payload, UserActivity.class);
                    if (activity.getIngestionTime() == null) activity.setIngestionTime(new Instant());
                    c.output(USER_ACTIVITY_TAG, activity);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown event type: " + eventType);
            }
        } catch (Exception e) {
            ErrorRecord error = ErrorRecord.builder()
                    .errorMessage("Parsing error: " + e.getMessage())
                    .originalJson(json)
                    .timestamp(new Instant())
                    .ingestionTime(new Instant())
                    .build();
            c.output(ERROR_TAG, error);
        }
    }
}
