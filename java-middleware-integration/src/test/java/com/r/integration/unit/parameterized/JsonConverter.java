package com.r.integration.unit.parameterized;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.r.integration.unit.JsonUnitUtils;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ArgumentConverter;

public class JsonConverter implements ArgumentConverter {
    @Override
    public Object convert(Object source, ParameterContext parameterContext) throws ArgumentConversionException {
        if(!(source instanceof JsonNode)){
            throw new ArgumentConversionException("Not a jsonNode");
        }
        JsonNode json = (JsonNode) source;
//        String name = parameterContext.getParameter().getName();
        Class<?> type = parameterContext.getParameter().getType();
        try {
            return JsonUnitUtils.parse(json, type);
        } catch (JsonProcessingException e) {
            throw new ArgumentConversionException("Can't convert to type: '" + type.getName() + "'");
        }

    }
}
