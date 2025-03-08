package com.elson.etl.schema;

import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableSchema;

import java.util.Arrays;

public class OrderTableSchema {
    public static TableSchema getSchema() {
        return new TableSchema().setFields(Arrays.asList(
                new TableFieldSchema().setName("event_type").setType("STRING"),
                new TableFieldSchema().setName("order_id").setType("STRING"),
                new TableFieldSchema().setName("customer_id").setType("STRING"),
                new TableFieldSchema().setName("order_date").setType("TIMESTAMP"),
                new TableFieldSchema().setName("status").setType("STRING"),
                new TableFieldSchema().setName("items").setType("RECORD")
                        .setMode("REPEATED")
                        .setFields(Arrays.asList(
                                new TableFieldSchema().setName("product_id").setType("STRING"),
                                new TableFieldSchema().setName("product_name").setType("STRING"),
                                new TableFieldSchema().setName("quantity").setType("INTEGER"),
                                new TableFieldSchema().setName("price").setType("FLOAT")
                        )),
                new TableFieldSchema().setName("shipping_address").setType("RECORD")
                        .setFields(Arrays.asList(
                                new TableFieldSchema().setName("street").setType("STRING"),
                                new TableFieldSchema().setName("city").setType("STRING"),
                                new TableFieldSchema().setName("country").setType("STRING")
                        )),
                new TableFieldSchema().setName("total_amount").setType("FLOAT"),
                new TableFieldSchema().setName("ingestion_time").setType("TIMESTAMP")
        ));
    }
}