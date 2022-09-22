package com.gsc.ninetosixapi.ninetosix.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CodeCheckReqDTO {
    String email;
    String fromType;
    String ranCode;
}
