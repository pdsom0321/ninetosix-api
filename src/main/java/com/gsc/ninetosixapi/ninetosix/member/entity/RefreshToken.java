package com.gsc.ninetosixapi.ninetosix.member.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RefreshToken {
    @Id
    private String email;

    private String token;

    public static RefreshToken create(String email, String token) {
        return RefreshToken.builder()
                .email(email)
                .token(token)
                .build();
    }

    public void updateToken(String token) {
        this.token = token;
    }
}