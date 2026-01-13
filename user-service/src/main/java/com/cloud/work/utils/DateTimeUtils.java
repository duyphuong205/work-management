package com.cloud.work.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class DateTimeUtils {

    public static String parseString(Timestamp timestamp, String pattern) {
        if (Objects.isNull(timestamp)) {
            return null;
        }
        return new SimpleDateFormat(pattern).format(timestamp);
    }
}
