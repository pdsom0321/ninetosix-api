package com.gsc.ninetosixapi.ninetosix.member.dto;

import com.gsc.ninetosixapi.ninetosix.member.entity.Member;

public record MyPageResDTO(String email, String name, String contact, String companyName) {
    public static MyPageResDTO of(Member member) {
        return new MyPageResDTO(member.getEmail(), member.getName(), member.getContact(), member.getCompany().getName());
    }
}
