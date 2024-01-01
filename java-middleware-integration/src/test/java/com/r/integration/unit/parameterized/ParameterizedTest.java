package com.r.integration.unit.parameterized;

import com.fasterxml.jackson.databind.JsonNode;
import com.r.integration.unit.JsonDTO;
import com.r.integration.unit.JsonDTO2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ArgumentsSources;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

class ParameterizedTest {
    @org.junit.jupiter.params.ParameterizedTest
    @JsonFileArgumentsSource(
            value = {
                    @JsonFileArgumentSource(resources = {"/json/testjson.json","/json/testjson2.json"}),
//                    @JsonFileArgumentSource(resources = {"/json/testjson.json","/json/testjson2.json"})
            }
    )

    void jsonFileTest(JsonDTO jsonDTO1,JsonDTO jsonDTO2){
        Assertions.assertNotNull(jsonDTO1);
        Assertions.assertNotNull(jsonDTO2);
    }
    @org.junit.jupiter.params.ParameterizedTest
//    @JsonFileArgumentSource(resources = {"/json/testjson.json","/json/testjson2.json"})
    @JsonFileArgumentSource(resources = {"/json/testjson.json","/json/testjson2.json"})
    void jsonFileTest2(JsonDTO jsonDTO1, JsonDTO jsonDTO2){
        Assertions.assertNotNull(jsonDTO1);
        Assertions.assertNotNull(jsonDTO2);
    }
}
