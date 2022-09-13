package com.gsc.ninetosixapi.ninetosix.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PwdChangeReqDTO {
    String email;
    String password;
}
