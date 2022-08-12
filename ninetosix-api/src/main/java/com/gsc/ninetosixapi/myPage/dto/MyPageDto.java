package com.gsc.ninetosixapi.myPage.dto;

import com.gsc.ninetosixapi.user.dto.UserInfoDTO;
import com.gsc.ninetosixapi.user.entity.User;
import lombok.*;

import java.util.Optional;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPageDto {
    private String email;
    private String name;
    private String contact;
    private String companyCode;
    private String pushAgreeYN;

    // AllArgsConstructor 사용
    /*public MyPageDto(String name, String email, String contact, String employeeNumber, String companyCode, String departmentCode, String pushAgreeYN) {
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.employeeNumber = employeeNumber;
        this.companyCode = companyCode;
        this.departmentCode = departmentCode;
        this.pushAgreeYN = pushAgreeYN;
    }*/

    public static MyPageDto from(Optional<User> user) {
        return new MyPageDto(
            user.get().getName()
            , user.get().getEmail()
            , user.get().getContact()
            , user.get().getCompanyCode()
            , user.get().getPushAgreeYn()
        );
    }

    public static MyPageDto of(String name, String email, String contact, String companyCode, String pushAgreeYN){
        return new MyPageDto(
            name
            , email
            , contact
            , companyCode
            , pushAgreeYN
        );
    }
}
