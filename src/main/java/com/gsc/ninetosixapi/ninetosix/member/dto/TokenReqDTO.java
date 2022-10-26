package com.gsc.ninetosixapi.ninetosix.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
public class TokenReqDTO {
    private String accessToken;
    private String refreshToken;
}
