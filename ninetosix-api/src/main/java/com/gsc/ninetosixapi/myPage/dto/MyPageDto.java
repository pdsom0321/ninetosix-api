package com.gsc.ninetosixapi.myPage.dto;

import com.gsc.ninetosixapi.company.entity.Company;
import com.gsc.ninetosixapi.user.entity.User;
import com.gsc.ninetosixapi.user.vo.YNCode;
import lombok.*;

import java.util.Optional;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPageDto {
    private String email;
    private String name;
    private String contact;
    private Company company;
    private YNCode pushAgreeYN;

    public static MyPageDto from(Optional<User> user) {
        return MyPageDto
                .builder()
                .name(user.get().getName())
                .email(user.get().getEmail())
                .contact(user.get().getContact())
                .company(user.get().getCompany())
                .pushAgreeYN(user.get().getPushAgreeYn())
                .build();
    }

    public static MyPageDto of(String name, String email, String contact, Company company, YNCode pushAgreeYN){
        return MyPageDto
                .builder()
                .name(name)
                .email(email)
                .contact(contact)
                .company(company)
                .pushAgreeYN(pushAgreeYN)
                .build();
    }
}
