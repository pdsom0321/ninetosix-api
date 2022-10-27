package com.gsc.ninetosixapi.ninetosix.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SignupReqDTO {
        private String email;
        private String name;
        private String password;
        private String contact;
        private String companyCode;
        private String pushAgreeYn;
}
