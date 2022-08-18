package com.gsc.ninetosixapi.core.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@RequiredArgsConstructor
@Converter
public class PasswordEncryptConverter implements AttributeConverter<String, String> {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String convertToDatabaseColumn(String entityAttribute) {
        return passwordEncoder.encode(entityAttribute);
    }

    @Override
    public String convertToEntityAttribute(String databaseColumn) {
        // DB 에서 가져올 때는 복호화하지 않음
        return databaseColumn;
    }
}
