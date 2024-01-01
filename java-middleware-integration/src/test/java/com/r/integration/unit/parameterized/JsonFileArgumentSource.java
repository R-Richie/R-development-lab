package com.r.integration.unit.parameterized;

import org.junit.jupiter.params.provider.ArgumentsSource;

import java.lang.annotation.*;

@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Repeatable(JsonFileArgumentsSource.class)
@ArgumentsSource(JsonFileArgumentProvider.class)
public @interface JsonFileArgumentSource {
    /**
     * The json classpath resources to use as the sources of arguments; must not be empty unless files is non-empty.
     */
    String[] resources() default {};

    /**
     * The json files to use as the sources of arguments; must not be empty unless resources is non-empty.
     */
    String[] files() default {};
}
