package com.gsc.ninetosixapi.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UserInfoDTO {
    private String email;
    private String name;
    private String password;
    private String contact;
//    private String employeeCode;
    private String companyCode;
//    private String departmentCode;
    private String pushAgreeYn;
}
