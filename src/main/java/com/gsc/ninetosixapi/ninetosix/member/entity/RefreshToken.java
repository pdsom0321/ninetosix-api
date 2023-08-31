package com.gsc.ninetosixapi.ninetosix.member.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RefreshToken {
    @Id
    private String email;

    private String token;

    private Date expiryDate;

    private LocalDateTime insertDate;

    private LocalDateTime updateDate;

    public static RefreshToken create(String email, String token, long expireTime) {
        return RefreshToken.builder()
                .email(email)
                .token(token)
                .expiryDate(new Date(expireTime))
                .insertDate(LocalDateTime.now())
                .build();
    }

    public void updateToken(String token, long expireTime) {
        this.token = token;
        this.expiryDate = new Date(expireTime);
        this.updateDate = LocalDateTime.now();
    }
}