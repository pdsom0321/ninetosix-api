package com.gsc.ninetosixapi.vo;

public enum Role {
    ROLE_ADMIN("관리자"), ROLE_MEMBER("사용자");

    private String desc;

    Role(String desc) {
        this.desc = desc;
    }
}
