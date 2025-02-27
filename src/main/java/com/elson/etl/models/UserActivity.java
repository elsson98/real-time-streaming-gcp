package com.elson.etl.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.api.services.bigquery.model.TableRow;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.Instant;

import javax.annotation.Nullable;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserActivity implements Serializable {
    private static final long serialVersionUID = -4228362611378888727L;

    @JsonProperty("event_type")
    private String eventType;

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("activity_type")
    private ActivityType activityType;

    @JsonProperty("ip_address")
    private String ipAddress;

    @JsonProperty("user_agent")
    private String userAgent;

    @JsonProperty("timestamp")
    private String timestamp;

    @Nullable
    @JsonProperty("metadata")
    private Metadata metadata;

    private Instant ingestionTime;

    public TableRow toTableRow() {
        TableRow row = new TableRow();
        row.set("event_type", eventType);
        row.set("user_id", userId);
        row.set("activity_type", activityType.toValue());
        row.set("ip_address", ipAddress);
        row.set("user_agent", userAgent);
        row.set("timestamp", timestamp);
        if (metadata != null) {
            row.set("metadata", metadata.toTableRow());
        }
        if (ingestionTime != null) {
            row.set("ingestion_time", ingestionTime.toString());
        }
        return row;
    }
}