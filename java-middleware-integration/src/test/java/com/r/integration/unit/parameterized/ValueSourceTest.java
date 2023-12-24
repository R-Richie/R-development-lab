package com.r.integration.unit.parameterized;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class ValueSourceTest {
    @ParameterizedTest
    @ValueSource(ints ={1,2,3})
    void testWithValueSource(int argument){
        Assertions.assertTrue(argument > 0 && argument < 4);
    }
    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {" ","   ","\t","\n"})
    void nullEmptyAndBlankStrings(String text){
        Assertions.assertTrue(text == null || text.trim().isEmpty());
    }
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ","   ","\t","\n"})
    void nullEmptyAndBlankStrings2(String text){
        Assertions.assertTrue(text == null || text.trim().isEmpty());
    }

}
