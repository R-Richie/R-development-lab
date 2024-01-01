package com.r.integration.unit.parameterized;

import com.fasterxml.jackson.databind.JsonNode;
import com.r.integration.unit.JsonUnitUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.AnnotationBasedArgumentsProvider;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.platform.commons.util.Preconditions;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Stream;

@Slf4j
public class JsonFileArgumentProvider  extends AnnotationBasedArgumentsProvider<JsonFileArgumentSource> {
    private final BiFunction<Class<?>, String, InputStream> inputStreamBiProvider;
    JsonFileArgumentProvider() {
        this(Class::getResourceAsStream);
    }
    JsonFileArgumentProvider(BiFunction<Class<?>, String, InputStream> inputStreamProvider) {
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
    private InputStream openInputStream(ExtensionContext context, String resource){
        Class<?> testClass = context.getRequiredTestClass();
        InputStream inputStream = inputStreamBiProvider.apply(testClass, resource);
        return Preconditions.notNull(inputStream, ()->"Classpath resource does not exists:"+resource);
    }
    @Override
    protected Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext, JsonFileArgumentSource annotation) {
//                boolean isListCondition = stream(extensionContext.getRequiredTestMethod().getParameterTypes())
//                .anyMatch(List.class::isAssignableFrom);
        String[] resources = annotation.resources();
        Class<?>[] parameterTypes = extensionContext.getRequiredTestMethod().getParameterTypes();
        if(parameterTypes.length != resources.length){
            //todo
            throw new RuntimeException();
        }
        Object[] arguments = new Object[parameterTypes.length];
        for(int i = 0 ; i < parameterTypes.length ; i++){
            Class<?> parameterType = parameterTypes[i];
            InputStream inputStream = openInputStream(extensionContext, resources[i]);
            JsonNode value = JsonFileArgumentProvider.values(inputStream);
            if(value.isArray() && parameterType.isAssignableFrom(List.class)){
//               value.
            }else{
                try {
                    Object parse = JsonUnitUtils.parse(value, parameterType);
                    arguments[i] = parse;
                }catch (Exception e){
                    log.error("",e);
                    arguments[i] = null;
                }
            }
        }
        return Stream.of(Arguments.of(arguments));



    }
}
