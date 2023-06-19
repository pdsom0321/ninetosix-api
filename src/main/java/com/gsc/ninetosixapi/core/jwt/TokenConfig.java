package com.gsc.ninetosixapi.core.jwt;

public class TokenConfig {
    public static final String AUTHORITIES_KEY = "auth";
    public static final String BEARER_PREFIX = "Bearer";
    public static final String MEMBER_ID = "id";

    // TODO : 데이터 변경 가능성이 있는 설정값은 DB 데이터화
    static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;
    static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;
}