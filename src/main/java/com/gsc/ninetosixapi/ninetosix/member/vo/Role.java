package com.gsc.ninetosixapi.ninetosix.member.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Role {
    ROLE_ADMIN("관리자"), ROLE_MANAGER("팀장"), ROLE_MEMBER("팀원");

    private final String desc;
}
