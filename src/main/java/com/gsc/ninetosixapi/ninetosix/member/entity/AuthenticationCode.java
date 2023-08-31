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

    private LocalDateTime expiryDate;

    private Boolean isCodeEntered;

    @Transient
    private static final Long MAX_EXPIRE_TIME = 5L;

    public void isEntered() {
        this.isCodeEntered = true;
    }

    public static AuthenticationCode create(int code, String email, String type) {
        return AuthenticationCode.builder()
                .code(code)
                .email(email)
                .type(AuthenticationCodeType.valueOf(type))
                .expiryDate(LocalDateTime.now().plusMinutes(MAX_EXPIRE_TIME))
                .isCodeEntered(false)
                .build();
    }
}
