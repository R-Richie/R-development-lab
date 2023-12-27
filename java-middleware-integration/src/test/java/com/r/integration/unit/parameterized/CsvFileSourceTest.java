package com.r.integration.unit.parameterized;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

class CsvFileSourceTest {
    @ParameterizedTest
    @CsvFileSource(resources = "/two-column.csv",numLinesToSkip = 1)
    void testWithCsvFileSourceFromClasspath(String country, int reference){
        Assertions.assertNotNull(country);
        Assertions.assertNotEquals(0, reference);
    }
    @ParameterizedTest
    @CsvFileSource(files = "src/test/resources/two-column.csv", numLinesToSkip = 1)
    void testWithCsvFileSourceFromFile(String country,int reference){
        Assertions.assertNotNull(country);
        Assertions.assertNotEquals(0, reference);
    }
    @ParameterizedTest(name = "[{index}]{arguments}")
    @CsvFileSource(resources = "/two-column.csv", useHeadersInDisplayName = true)
    void testWithCsvFileSourceAndHeaders(String country, int reference){
        Assertions.assertNotNull(country);
        Assertions.assertNotEquals(0, reference);
    }
}
