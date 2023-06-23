package com.gsc.ninetosixapi.ninetosix.member.entity;

import com.gsc.ninetosixapi.ninetosix.member.vo.AuthenticationCodeType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authentication_code_id")
    private Long id;
    private int code;
    private String email;
    @Enumerated(EnumType.STRING)
    private AuthenticationCodeType type;
    private Boolean expired;
    private LocalDateTime expireDate;

    public void isDone() {
        this.expired = true;
    }
    @Transient
    private static final Long MAX_EXPIRE_TIME = 5L;

    public static AuthenticationCode create(int code, String email, String type) {
        return AuthenticationCode.builder()
                .code(code)
                .email(email)
                .type(AuthenticationCodeType.valueOf(type))
                .expireDate(LocalDateTime.now().plusMinutes(MAX_EXPIRE_TIME))
                .expired(false)
                .build();
    }
}
