package com.r.integration.unit.parameterized;

import com.r.integration.unit.JsonDTO;
import com.r.integration.unit.JsonDTO2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

class ParameterizedTest {
    @org.junit.jupiter.params.ParameterizedTest
    @JsonFileSource(resources = {"/json/testjson.json","/json/testjson2.json"})
@CsvSource
    void jsonFileTest(@ConvertWith(JsonConverter.class)JsonDTO jsonDTO1){
        Assertions.assertNotNull(jsonDTO1);
//        Assertions.assertNotNull(jsonDTO2);
    }
}
