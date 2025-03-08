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
public class Metadata implements Serializable {
    private static final long serialVersionUID = -4228362611378888727L;

    @JsonProperty("session_id")
    private String sessionId;

    @JsonProperty("platform")
    private Platform platform;

    public TableRow toTableRow() {
        TableRow row = new TableRow();
        row.set("session_id", sessionId);
        row.set("platform", platform != null ? platform.toValue() : null);
        return row;
    }
}