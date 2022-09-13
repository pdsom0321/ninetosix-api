package com.gsc.ninetosixapi.ninetosix.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CodeCheckReqDTO {
    String email;
    String fromType;
    String ranCode;
}
