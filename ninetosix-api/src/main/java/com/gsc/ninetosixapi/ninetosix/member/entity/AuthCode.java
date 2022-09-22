package com.gsc.ninetosixapi.ninetosix.member.entity;

import com.gsc.ninetosixapi.ninetosix.member.vo.AuthCodeFrom;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "auth_code_id")
    private Long id;

    private String email;
    private String ranCode;     // 인증번호
    private Boolean expired;    // 만료 여부
    private LocalDateTime expireDate;   // 만료 시간

    @Enumerated(EnumType.STRING)
    private AuthCodeFrom fromType;

    public void isTrue() {
        this.expired = true;
    }

    @Transient
    private static final Long MAX_EXPIRE_TIME = 5L;

    public static AuthCode createAuthCode(String email, String from, String ranCode) {
        return AuthCode.builder()
                .email(email)
                .ranCode(ranCode)
                .fromType(AuthCodeFrom.valueOf(from))
                .expireDate(LocalDateTime.now().plusMinutes(MAX_EXPIRE_TIME))
                .expired(false)
                .build();
    }
}
