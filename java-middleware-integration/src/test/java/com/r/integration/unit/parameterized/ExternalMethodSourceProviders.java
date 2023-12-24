package com.r.integration.unit.parameterized;

import java.util.stream.Stream;

public class ExternalMethodSourceProviders {
    static Stream<String> tinyStrings(){
        return Stream.of(".","oo","000");
    }
}
