package com.gsc.ninetosixapi.ninetosix.member.dto;

import com.gsc.ninetosixapi.ninetosix.member.entity.Member;

public record MemberInfoResDTO(String email, String name, String contact, String companyName) {
    public static MemberInfoResDTO of(Member member) {
        return new MemberInfoResDTO(member.getEmail(), member.getName(), member.getContact(), member.getCompany().getName());
    }
}
