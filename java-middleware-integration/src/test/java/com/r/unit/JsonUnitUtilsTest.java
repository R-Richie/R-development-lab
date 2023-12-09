package com.r.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class JsonUnitUtilsTest {
    @Test
    void testParse() throws IOException {
        String name  = "123";
        String code = "456";
        JsonDTO parse = JsonUnitUtils.parse("/json/testjson.json", JsonDTO.class);
        Assertions.assertEquals(code, parse.getCode());
        Assertions.assertEquals(name, parse.getName());
    }
}
