package com.elson.etl.pipeline;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.beam.sdk.io.FileSystems;
import org.apache.beam.sdk.transforms.DoFn;
import org.joda.time.Instant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class GcsWriter extends DoFn<String, Void> {

    private static final String BUCKET_NAME = "inventory-ingestion-etl";
    private static final String OUTPUT_PREFIX = "output";

    private transient DateTimeFormatter folderFormatter;
    private transient DateTimeFormatter fileFormatter;
    private transient ObjectMapper objectMapper;

    @Setup
    public void setup() {
        folderFormatter = DateTimeFormat.forPattern("yyyy/MM/dd/HH/mm").withZoneUTC();
        fileFormatter = DateTimeFormat.forPattern("yyyyMMddHHmmss").withZoneUTC();
        objectMapper = new ObjectMapper();
    }

    @ProcessElement
    public void processElement(ProcessContext context) throws IOException {
        String json = context.element();
        Instant now = Instant.now();
        String subFolder = determineSubFolder(json);
        String partitionPath = folderFormatter.print(now);

        String fileName = String.format(
                "%s_%s_%s.json",
                subFolder,
                fileFormatter.print(now),
                UUID.randomUUID().toString().substring(0, 8)
        );

        String filePath = String.format(
                "gs://%s/%s/%s/%s/%s",
                BUCKET_NAME,
                OUTPUT_PREFIX,
                subFolder,
                partitionPath,
                fileName
        );

        try (WritableByteChannel channel = FileSystems.create(
                FileSystems.matchNewResource(filePath, false), "text/plain")) {
            channel.write(ByteBuffer.wrap(json.getBytes(StandardCharsets.UTF_8)));
        }
    }


    private String determineSubFolder(String json) {
        try {
            JsonNode root = objectMapper.readTree(json);
            Iterator<Map.Entry<String, JsonNode>> fields = root.fields();

            if (fields.hasNext()) {
                String eventNodeName = fields.next().getKey();
                switch (eventNodeName) {
                    case "OrderEvent":
                        return "order";
                    case "InventoryEvent":
                        return "inventory";
                    case "UserActivityEvent":
                        return "user_activity";
                    default:
                        throw new IllegalArgumentException(
                                "Unknown event type: " + eventNodeName
                        );
                }
            } else {
                throw new IllegalArgumentException(
                        "No top-level event field found in JSON"
                );
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(
                    "Failed to parse JSON: " + e.getMessage(),
                    e
            );
        }
    }
}