package com.r.integration.unit.parameterized;

import com.fasterxml.jackson.databind.JsonNode;
import com.r.integration.unit.JsonUnitUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.AnnotationBasedArgumentsProvider;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.support.AnnotationConsumer;
import org.junit.platform.commons.util.Preconditions;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static java.util.Arrays.stream;
@Slf4j
public class JsonFileArgumentsProvider extends AnnotationBasedArgumentsProvider<JsonFileArgumentsSource> {

    private final BiFunction<Class<?>, String, InputStream> inputStreamBiProvider;
    private boolean useClassPath;
    private String[][] resources;



    JsonFileArgumentsProvider() {
        this(Class::getResourceAsStream);
    }
    JsonFileArgumentsProvider(BiFunction<Class<?>, String, InputStream> inputStreamProvider) {
        this.inputStreamBiProvider = inputStreamProvider;
    }
    private static JsonNode values(InputStream inputStream) {
        JsonNode parse = null;
        try {
            parse = JsonUnitUtils.parse(inputStream);
        }catch (IOException e){
            log.error("json read error",e);
        }
        return parse;
    }
//    @Override
//    public void accept(JsonFileArgumentsSource jsonFileArgumentsSource) {
//        JsonFileArgumentSource[] value = jsonFileArgumentsSource.value();
//        if(value == null || value.length < 1){
//            //todo exception
//            throw new RuntimeException("source less 1");
//        }
//        resources = new String[value.length][];
//        String[] resourceOne = value[0].resources();
//        boolean useFiles = resourceOne == null || resourceOne.length == 0;
//        useClassPath = !useFiles;
//
//        for (int i = 0; i < value.length; i++) {
//            if(useClassPath) {
//                resources[i] = value[i].resources();
//            }else{
//                resources[i] = value[i].files();
//            }
//        }
//    }

//    @Override
//    public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext){
//        boolean isListCondition = stream(extensionContext.getRequiredTestMethod().getParameterTypes())
//                .anyMatch(List.class::isAssignableFrom);
//
//        return stream(resources)
//                .map(argument -> stream(argument)
//                        .map(oneItem -> openInputStream(extensionContext, oneItem))
//                        .map(JsonFileArgumentsProvider::values)
//                        .flatMap(json-> {
//                            if (json.isArray() && !isListCondition) {
//                                return StreamSupport.stream(json.spliterator(), false);
//                            }
//                            return Stream.of(json);
//                        })
//                )
//                .map(Arguments::arguments);
////        return stream(resources)
////                .map(resource ->  openInputStream(extensionContext, resource))
////                .map(JsonFileArgumentsProvider::values)
////                .flatMap(json->{
////                    if(json.isArray() && !isListCondition){
////                        return StreamSupport.stream(json.spliterator(),false);
////                    }
////                    return Stream.of(json);
////                })
////                .map(Arguments::arguments);
//
//    }

    @Override
    protected Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext, JsonFileArgumentsSource annotation) {
                boolean isListCondition = stream(extensionContext.getRequiredTestMethod().getParameterTypes())
                .anyMatch(List.class::isAssignableFrom);
        JsonFileArgumentSource[] value = annotation.value();
        if(value == null || value.length < 1){
            //todo exception
            throw new RuntimeException("source less 1");
        }
        resources = new String[value.length][];
        String[] resourceOne = value[0].resources();
        boolean useFiles = resourceOne == null || resourceOne.length == 0;
        useClassPath = !useFiles;

        for (int i = 0; i < value.length; i++) {
            if(useClassPath) {
                resources[i] = value[i].resources();
            }else{
                resources[i] = value[i].files();
            }
        }
        return stream(resources)
                .map(argument -> stream(argument)
                        .map(oneItem -> openInputStream(extensionContext, oneItem))
                        .map(JsonFileArgumentsProvider::values)
                        .flatMap(json-> {
                            if (json.isArray() && !isListCondition) {
                                return StreamSupport.stream(json.spliterator(), false);
                            }
                            return Stream.of(json);
                        })
                )
                .map(Arguments::arguments);
    }

    private InputStream openInputStream(ExtensionContext context, String resource){
        Class<?> testClass = context.getRequiredTestClass();
        InputStream inputStream = inputStreamBiProvider.apply(testClass, resource);
        return Preconditions.notNull(inputStream, ()->"Classpath resource does not exists:"+resource);
    }
}
