package com.r.integration.unit.parameterized;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

class MethodSourceTest {
    @ParameterizedTest
    @MethodSource("stringProvider")
    void testWithExplicitLocalMethodSource(String argument){
        Assertions.assertNotNull(argument);
    }

    static Stream<String> stringProvider(){
        return Stream.of("apple","banana");
    }
    @ParameterizedTest
    @MethodSource
    void testWithDefaultLocalMethodSource(String argument){
        Assertions.assertNotNull(argument);
    }
    static Stream<String> testWithDefaultLocalMethodSource(){
        return Stream.of("apple","banana");
    }
    @ParameterizedTest
    @MethodSource("range")
    void testWithRangeMethodSource(int argument){
        Assertions.assertNotEquals(9, argument);
    }
    static IntStream range(){
        return IntStream.range(0,20).skip(10);
    }
    @ParameterizedTest
    @MethodSource("stringIntAndListProvider")
    void testWithMultiArgMethodSource(String str, int num, List<String> list){
        Assertions.assertEquals(5, str.length());
        Assertions.assertTrue(num >=1 && num <=2);
        Assertions.assertEquals(2, list.size());
    }
    static Stream<Arguments> stringIntAndListProvider(){
        return Stream.of(
                Arguments.arguments("apple",1, Arrays.asList("a","b")),
                Arguments.arguments("lemon",2, Arrays.asList("x","y"))
        );
    }
    @ParameterizedTest
    @MethodSource("com.r.integration.unit.parameterized.ExternalMethodSourceProviders#tinyStrings")
    void testWithExternalMethodSource(String tinyString){
        Assertions.assertNotNull(tinyString);
    }
    @RegisterExtension
    static final IntegerResolver integerResolver = new IntegerResolver();

    @ParameterizedTest
    @MethodSource("factoryMethodWithArguments")
    void testWithFactoryMethodWithArguments(String argument){
        Assertions.assertTrue(argument.startsWith("2"));
    }
    static Stream<Arguments> factoryMethodWithArguments(int quantity){
        return Stream.of(
                Arguments.arguments(quantity + " apples"),
                Arguments.arguments(quantity+ " lemons")
        );
    }

    static class IntegerResolver implements ParameterResolver {
        @Override
        public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
            return parameterContext.getParameter().getType() == int.class;
        }

        @Override
        public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
            return 2;
        }
    }

}
