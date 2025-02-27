package com.elson.etl.schema;

import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableSchema;

import java.util.Arrays;

public class InventoryTableSchema {
    public static TableSchema getSchema() {
        return new TableSchema().setFields(Arrays.asList(
                new TableFieldSchema().setName("event_type").setType("STRING"),
                new TableFieldSchema().setName("inventory_id").setType("STRING"),
                new TableFieldSchema().setName("product_id").setType("STRING"),
                new TableFieldSchema().setName("warehouse_id").setType("STRING"),
                new TableFieldSchema().setName("quantity_change").setType("INTEGER"),
                new TableFieldSchema().setName("reason").setType("STRING"),
                new TableFieldSchema().setName("event_timestamp").setType("TIMESTAMP"),
                new TableFieldSchema().setName("ingestion_time").setType("TIMESTAMP")
        ));
    }
}