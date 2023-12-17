package com.r.integration.unit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.InputStream;

public class JsonUnitUtils {
    private final static ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    public static <T> T parse(String jsonPath, Class<T> valueType) throws IOException {
        return mapper.readValue(
                JsonUnitUtils.class.getResourceAsStream(jsonPath), valueType);
    }

    public static JsonNode parse(InputStream inputStream) throws IOException {
        return mapper.readTree(inputStream);
    }

    public static <T> T parse(JsonNode node, Class<T> resultClass) throws JsonProcessingException {
        return mapper.treeToValue(node, resultClass);
    }
}
