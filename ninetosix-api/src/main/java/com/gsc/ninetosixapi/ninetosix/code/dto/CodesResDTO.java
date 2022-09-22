package com.gsc.ninetosixapi.ninetosix.code.dto;

import com.gsc.ninetosixapi.ninetosix.code.entity.Code;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CodesResDTO {
    String code;
    String name;

    public static CodesResDTO getCodes(Code code) {
        return CodesResDTO.builder()
                .code(code.getCode())
                .name(code.getName())
                .build();
    }
}
