package com.cloud.work.utils;

import java.util.Map;

public class StringUtils {

    public static String messageReplace(String content, Map<String, ?> params) {
        if (content == null || params == null) {
            return content;
        }
        for (var entry : params.entrySet()) {
            if (entry.getValue() != null) {
                content = content.replace(entry.getKey(), entry.getValue().toString());
            }
        }
        return content;
    }
}
