package com.gsc.ninetosixapi.ninetosix.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CodeCheckRequestDTO {
    String email;
    String fromType;
    String ranCode;
}
