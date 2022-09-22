package com.gsc.ninetosixapi.ninetosix.myPage.dto;

import com.gsc.ninetosixapi.ninetosix.company.entity.Company;
import com.gsc.ninetosixapi.ninetosix.member.entity.Member;
import com.gsc.ninetosixapi.ninetosix.vo.YNCode;
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

    public static MyPageDto from(Optional<Member> member) {
        return MyPageDto
                .builder()
                .name(member.get().getName())
                .email(member.get().getEmail())
                .contact(member.get().getContact())
                .company(member.get().getCompany())
                .pushAgreeYN(member.get().getPushAgreeYn())
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
