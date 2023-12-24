package com.r.integration.unit.parameterized;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class CsvSourceTest {
    @ParameterizedTest
    @CsvSource({
            "apple,         1",
            "banana,        2",
            "'lemon, lime', 0xF1",
            "strawberry,    700_000"
    })
    void testWithCsvSource(String fruit, int rank){
        Assertions.assertNotNull(fruit);
        Assertions.assertNotEquals(0, rank);
    }


}
