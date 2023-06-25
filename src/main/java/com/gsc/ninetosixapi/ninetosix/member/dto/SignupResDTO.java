package com.gsc.ninetosixapi.ninetosix.member.dto;

import com.gsc.ninetosixapi.ninetosix.member.entity.Member;
public record SignupResDTO(String email, String name) {
    public static SignupResDTO of(Member member) {
        return new SignupResDTO(member.getEmail(), member.getName());
    }
}
