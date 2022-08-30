package com.gsc.ninetosixapi.ninetosix.user.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailAuth {

    private static final Long MAX_EXPIRE_TIME = 5L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_auth_id")
    private Long id;

    private String email;
    private String authToken;   // UUID
    private Boolean expired;    // 만료 여부
    private LocalDateTime expireDate;   // 만료 시간

    public void useToken() {
        this.expired = true;
    }
}
