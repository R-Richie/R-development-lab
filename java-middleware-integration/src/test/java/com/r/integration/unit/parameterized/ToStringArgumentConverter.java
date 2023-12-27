package com.r.integration.unit.parameterized;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;

public class ToStringArgumentConverter extends SimpleArgumentConverter {
    @Override
    protected Object convert(Object source, Class<?> targetType) throws ArgumentConversionException {
        Assertions.assertEquals(String.class, targetType, "Can only convert to String");
        if(source instanceof Enum<?>){
            return ((Enum<?>)source).name();
        }
        return String.valueOf(source);
    }
}
