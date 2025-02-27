package com.elson.etl.schema;

import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableSchema;

import java.util.Arrays;

public class ErrorTableSchema {
    public static TableSchema getSchema() {
        return new TableSchema().setFields(Arrays.asList(
                new TableFieldSchema().setName("error_message").setType("STRING"),
                new TableFieldSchema().setName("original_json").setType("STRING"),
                new TableFieldSchema().setName("timestamp").setType("TIMESTAMP"),
                new TableFieldSchema().setName("ingestion_time").setType("TIMESTAMP")
        ));
    }
}