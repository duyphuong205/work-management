package com.cloud.work.utils;

import com.cloud.work.converter.StringToTimestampConverter;
import com.cloud.work.converter.TimestampToStringConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class ObjectConvertUtils {

    public static <D> D convertToDTO(Object object, Class<D> clazz) {
        if(object == null){
            return null;
        }
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.addConverter(new TimestampToStringConverter());
        return modelMapper.map(object, clazz);
    }

    public static <D> D convertToEntities(Object object, Class<D> clazz) {
        if(object == null){
            return null;
        }
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.addConverter(new StringToTimestampConverter());
        return modelMapper.map(object, clazz);
    }
}
