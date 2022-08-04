package com.gsc.ninetosixapi.myPage.dto;

import com.gsc.ninetosixapi.user.dto.UserInfoDTO;
import com.gsc.ninetosixapi.user.entity.User;

public class MyPageDto extends UserInfoDTO {
    public MyPageDto(String email, String name, String contact, String empNo, String compCd, String deptCd, String pushAgreeYN){
        super(email, name, null, contact, empNo, compCd, deptCd, pushAgreeYN);
    }
}
