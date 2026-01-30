package com.cloud.work.converter;

import com.cloud.work.utils.DateTimeUtils;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;

import java.sql.Timestamp;

public class StringToTimestampConverter implements Converter<String, Timestamp> {

    @Override
    public Timestamp convert(MappingContext<String, Timestamp> mappingContext) {
        String source = mappingContext.getSource();
        return DateTimeUtils.parseTimestamp(source);
    }
}
