package com.gsc.ninetosixapi.ninetosix.member.dto;

import lombok.Builder;

@Builder
public record LoginResDTO(String grantType, String accessToken, String refreshToken, Long id, String name, Boolean isPasswordExpired) {}
