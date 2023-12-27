package com.r.integration.unit.parameterized;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.JavaTimeConversionPattern;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
class ArgumentConversionTest {
    /**
     * widening conversion,int to long,float,double
     * @param argument
     */
    @ParameterizedTest
    @ValueSource(ints ={1,2,3})
    void testWithValueSource(long argument){
        Assertions.assertTrue(argument > 0 && argument < 4);
    }
    /**
     * implicit conversion
     * @param argument
     */
    @ParameterizedTest
    @ValueSource(strings = "SECONDS")
    void testWithImplicitArgumentConversion(ChronoUnit argument){
        Assertions.assertNotNull(argument.name());
    }

    /**
     * Explicit argument
     * @param book
     */
    @ParameterizedTest
    @ValueSource(strings = "42 Cats")
    void testWithImplicitFallbackArgumentConversion(Book book){
        Assertions.assertEquals("42 Cats", book.getTitle());
    }
    @ParameterizedTest
    @EnumSource(ChronoUnit.class)
    void testWithExplicitArgumentConversion(@ConvertWith(ToStringArgumentConverter.class) String argument){
        Assertions.assertNotNull(ChronoUnit.valueOf(argument));
    }
    @ParameterizedTest
    @ValueSource(strings = {"01.01.2017","31.12.2017"})
    void testWithExplicitJavaTimeConverter(@JavaTimeConversionPattern("dd.MM.yyyy") LocalDate argument){
        Assertions.assertEquals(2017, argument.getYear());
    }
}
