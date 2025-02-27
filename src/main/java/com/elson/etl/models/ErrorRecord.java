package com.elson.etl.models;

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
public class ErrorRecord implements Serializable {
    private static final long serialVersionUID = -4228362611378888727L;

    private String errorMessage;
    private String originalJson;
    private Instant timestamp;
    private Instant ingestionTime;

    public TableRow toTableRow() {
        TableRow row = new TableRow();
        row.set("error_message", errorMessage);
        row.set("original_json", originalJson);
        row.set("timestamp", timestamp != null ? timestamp.toString() : null);
        row.set("ingestion_time", ingestionTime != null ? ingestionTime.toString() : null);
        return row;
    }
}