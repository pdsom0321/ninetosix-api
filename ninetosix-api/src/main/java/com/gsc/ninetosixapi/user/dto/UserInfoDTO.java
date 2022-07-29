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
    private String pwd;
    private String contact;
    private String empNo;
    private String compCd;
    private String deptCd;
    private String pushAgreeYN;
}
