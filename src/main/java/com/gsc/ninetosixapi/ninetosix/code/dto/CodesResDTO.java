package com.gsc.ninetosixapi.ninetosix.code.dto;

import com.gsc.ninetosixapi.ninetosix.code.entity.Code;
public record CodesResDTO(String code, String name) {
    public static CodesResDTO of(Code code) {
        return new CodesResDTO(code.getCode(), code.getName());
    }
}
