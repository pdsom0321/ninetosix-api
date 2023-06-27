package com.gsc.ninetosixapi.ninetosix.member.dto;

public record ReissueResDTO(String accessToken, String refreshToken) {
    public static ReissueResDTO of(String accessToken, String refreshToken) {
        return new ReissueResDTO(accessToken, refreshToken);
    }
}
