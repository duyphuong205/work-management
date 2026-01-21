package com.cloud.work.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ObjectConvertUtils {
    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String toJson(Object o) {
        try {
            if (o == null) {
                return null;
            }
            return OBJECT_MAPPER.writeValueAsString(o);
        } catch (Exception e) {
            log.error(">>>ObjectConvertUtils toJson() ERROR", e);
            return null;
        }
    }

    public static <T> T toObject(String s, Class<T> clazz) {
        try {
            if (s == null) {
                return null;
            }
            return OBJECT_MAPPER.readValue(s, clazz);
        } catch (Exception e) {
            log.error(">>>ObjectConvertUtils toObject() ERROR", e);
            return null;
        }
    }
}
