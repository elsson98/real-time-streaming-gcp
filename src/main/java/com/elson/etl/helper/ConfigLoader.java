package com.elson.etl.helper;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigLoader {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static Map<String, String> configMap;


    public static void loadConfig(String filePath) throws IOException {
        File configFile = new File(filePath);
        JsonNode rootNode = MAPPER.readTree(configFile);

        configMap = new HashMap<>();
        rootNode.fields().forEachRemaining(entry -> {
            configMap.put(entry.getKey(), entry.getValue().asText());
        });
    }


    public static String getConfig(String key) {
        if (configMap == null || !configMap.containsKey(key)) {
            throw new IllegalArgumentException("Configuration key not found: " + key);
        }
        return configMap.get(key);
    }

    public static String getConfig(String key, String defaultValue) {
        return configMap != null && configMap.containsKey(key) ? configMap.get(key) : defaultValue;
    }
}