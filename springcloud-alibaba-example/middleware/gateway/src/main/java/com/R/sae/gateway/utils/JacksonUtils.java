package com.R.sae.gateway.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;

public class JacksonUtils {
    private static final JacksonUtils INSTANCE = new JacksonUtils();

    private static final ObjectMapper JACKSON = Jackson2ObjectMapperBuilder.json()
            .failOnUnknownProperties(false)
            .build();

    public static JacksonUtils getInstance(){return INSTANCE;}
    public static ObjectMapper getJson(){return JacksonUtils.JACKSON;}
    public String toJson(final Object object) throws JsonProcessingException{
        return JACKSON.writeValueAsString(object);
    }
    public <T> T fromJson(final String json, final Class<T> tClass) throws  JsonProcessingException{
        return JACKSON.readValue(json, tClass);
    }

    public <T> T fromJson(final JsonNode jsonNode, final Class<T> tClass) throws IOException{
        return JACKSON.treeToValue(jsonNode, tClass);
    }
}
