package com.shop.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.FeatureDescriptor;
import java.util.stream.Stream;

public class EntityUtil {
    public static void copyProperties(Object source, Object target, boolean ignoreNullValue, String... ignoreProperties) {
        if (ignoreNullValue) {
            BeanWrapper wrappedSource = new BeanWrapperImpl(source);
            String[] nullProps = Stream.of(wrappedSource.getPropertyDescriptors()).map(FeatureDescriptor::getName)
                    .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null).toArray(String[]::new);
            ignoreProperties = ArrayUtils.addAll(ignoreProperties, nullProps);
        }
        BeanUtils.copyProperties(source, target, ignoreProperties);
    }
}
