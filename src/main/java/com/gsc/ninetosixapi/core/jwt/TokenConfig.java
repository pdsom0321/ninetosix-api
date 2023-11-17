package com.gsc.ninetosixapi.core.jwt;

import java.util.concurrent.TimeUnit;

public class TokenConfig {
    public static final String AUTHORITIES_KEY = "auth";
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer";
    public static final String MEMBER_ID = "id";

    public static final String UUID_HEADER = "X-UUID-1";
    public static final String IV_HEADER = "X-UUID-2";
    public static final String DAY_HEADER = "X-UUID-3";
    public static final String AUTH_HEADER = "X-AUTH";

    // TODO : 데이터 변경 가능성이 있는 설정값은 DB 데이터화
    public static final long ACCESS_TOKEN_EXPIRE_TIME = TimeUnit.DAYS.toMillis(3);
    public static final long REFRESH_TOKEN_EXPIRE_TIME = TimeUnit.DAYS.toMillis(10);
}