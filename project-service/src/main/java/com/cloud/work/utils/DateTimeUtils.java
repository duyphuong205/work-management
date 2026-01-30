package com.cloud.work.utils;

import com.cloud.work.constants.AppConstants;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class DateTimeUtils {
    private static final SimpleDateFormat SDF = new SimpleDateFormat(AppConstants.FORMAT_DATE_DD_MM_YYYY);

    public static String parseString(Timestamp timestamp, String pattern) {
        if (Objects.isNull(timestamp)) {
            return null;
        }
        return new SimpleDateFormat(pattern).format(timestamp);
    }

    public static Date parseDate(String s) {
        try {
            return SDF.parse(s);
        } catch (Exception e) {
            return null;
        }
    }

    public static String parseString(Timestamp timestamp) {
        try {
            if (Objects.isNull(timestamp)) {
                return null;
            }
            return SDF.format(timestamp);
        } catch (Exception e) {
            return null;
        }
    }

    public static Timestamp parseTimestamp(String timestamp) {
        try {
            if (Objects.isNull(timestamp)) {
                return null;
            }
            Date date = DateTimeUtils.parseDate(timestamp);
            if (Objects.isNull(date)) {
                return null;
            }
            long time = date.getTime();
            return new Timestamp(time);
        } catch (Exception e) {
            return null;
        }
    }
}
