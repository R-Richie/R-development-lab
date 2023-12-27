package com.r.integration.unit.parameterized;

import org.junit.jupiter.params.converter.TypedArgumentConverter;

public class ToLengthArgumentConverter extends TypedArgumentConverter<String, Integer> {
    protected ToLengthArgumentConverter(){
        super(String.class, Integer.class);
    }
    @Override
    protected Integer convert(String source){
        return (source != null ? source.length() : 0);
    }
}
