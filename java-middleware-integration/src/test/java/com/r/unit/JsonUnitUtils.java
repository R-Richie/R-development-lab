package com.r.unit;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

public class JsonUnitUtils {
    private final static ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    public static <T> T parse(String jsonPath, Class<T> valueType) throws IOException {
        return mapper.readValue(
                JsonUnitUtils.class.getResourceAsStream(jsonPath), valueType);
    }
}