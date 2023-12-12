package com.r.sae.gateway.utils;

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
        String s = "";
        try{
            s = JACKSON.writeValueAsString(object);
        }catch ( JsonProcessingException e){
            throw new RuntimeException("to json error",e);
        }
        return s;
    }
    public <T> T fromJson(final String json, final Class<T> tClass){
        T t = null;
        try{
            t = JACKSON.readValue(json, tClass);
        }catch (JsonProcessingException e){
            throw new RuntimeException("json error", e);
        }
        return t;
    }

    public <T> T fromJson(final JsonNode jsonNode, final Class<T> tClass) throws IOException{
        return JACKSON.treeToValue(jsonNode, tClass);
    }
}
