package com.zhlzzz.together.auth.oauth;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

@Converter
public class ScopeStringConverter implements AttributeConverter<Set<String>, String> {

    @Override
    public String convertToDatabaseColumn(Set<String> attribute) {
        return String.join(" ", attribute);
    }

    @Override
    public Set<String> convertToEntityAttribute(String dbData) {
        return new TreeSet<>(Arrays.asList(dbData.split(" ")));
    }
}
