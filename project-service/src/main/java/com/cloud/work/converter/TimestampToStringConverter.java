package com.cloud.work.converter;

import com.cloud.work.utils.DateTimeUtils;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.sql.Timestamp;

public class TimestampToStringConverter implements Converter<Timestamp, String> {

    @Override
    public String convert(MappingContext<Timestamp, String> context) {
        Timestamp timestamp = context.getSource();
        return DateTimeUtils.parseString(timestamp);
    }
}
