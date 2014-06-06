package com.cosooki.topcheck.tools;

import java.io.InputStream;
import java.lang.reflect.Method;


public class Parser {
    static final String PARSER_METHOD = "parse";

    public <T> T parse(Class<T> clazz, InputStream in) throws ParseException {

        Method m;
        T result = null;
        try {
            m = clazz.getDeclaredMethod(PARSER_METHOD, InputStream.class);
            Object o = m.invoke(null, in);
            result = (T) o;
            return result;
        } catch (Exception e) {
            throw new ParseException(-1, "Parse Exception");
        }
    }
}
