package com.gsc.ninetosixapi.ninetosix.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CodeCheckReqDTO {
    private String email;
    private String fromType;
    private String ranCode;
}
