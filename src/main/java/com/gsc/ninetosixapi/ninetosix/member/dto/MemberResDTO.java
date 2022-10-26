package com.gsc.ninetosixapi.ninetosix.member.dto;

import com.gsc.ninetosixapi.ninetosix.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MemberResDTO {
    private String email;
    private String name;

    public static MemberResDTO of(Member member) {
        return new MemberResDTO(member.getEmail(), member.getName());
    }
}
