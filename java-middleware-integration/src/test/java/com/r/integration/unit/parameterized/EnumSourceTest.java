package com.r.integration.unit.parameterized;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.EnumSet;

class EnumSourceTest {
    @ParameterizedTest
    @EnumSource(ChronoUnit.class)
    void testWithEnumSource(TemporalUnit unit){
        Assertions.assertNotNull(unit);
    }
    @ParameterizedTest
    @EnumSource
    void testWithEnumSourceWithAutoDetection(ChronoUnit unit){
        Assertions.assertNotNull(unit);
    }
    @ParameterizedTest
    @EnumSource(names = {"DAYS","HOURS"})
    void testWithEnumSourceInclude(ChronoUnit unit){
        Assertions.assertTrue(EnumSet.of(ChronoUnit.DAYS, ChronoUnit.HOURS).contains(unit));
    }
    @ParameterizedTest
    @EnumSource(mode = EnumSource.Mode.EXCLUDE, names = {"ERAS","FOREVER"})
    void testWithEnumSourceExclude(ChronoUnit unit){
        Assertions.assertFalse(EnumSet.of(ChronoUnit.ERAS,ChronoUnit.FOREVER).contains(unit));
    }

    @ParameterizedTest
    @EnumSource(mode = EnumSource.Mode.MATCH_ALL, names = "^.*DAYS$")
    void testWithEnumSourceRegex(ChronoUnit unit){
        Assertions.assertTrue(unit.name().endsWith("DAYS"));
    }
}
