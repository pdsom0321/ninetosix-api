package com.gsc.ninetosixapi.ninetosix.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class signupReqDTO {
    private String email;
    private String name;
    private String password;
    private String contact;
    private String companyCode;
    private String pushAgreeYn;
}
