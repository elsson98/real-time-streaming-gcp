package com.elson.etl.schema;

import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableSchema;

import java.util.Arrays;

public class UserActivityTableSchema {
    public static TableSchema getSchema() {
        return new TableSchema().setFields(Arrays.asList(
                new TableFieldSchema().setName("event_type").setType("STRING"),
                new TableFieldSchema().setName("user_id").setType("STRING"),
                new TableFieldSchema().setName("activity_type").setType("STRING"),
                new TableFieldSchema().setName("ip_address").setType("STRING"),
                new TableFieldSchema().setName("user_agent").setType("STRING"),
                new TableFieldSchema().setName("event_timestamp").setType("TIMESTAMP"),
                new TableFieldSchema().setName("metadata").setType("RECORD")
                        .setFields(Arrays.asList(
                                new TableFieldSchema().setName("session_id").setType("STRING"),
                                new TableFieldSchema().setName("platform").setType("STRING")
                        )),
                new TableFieldSchema().setName("ingestion_time").setType("TIMESTAMP")
        ));
    }
}