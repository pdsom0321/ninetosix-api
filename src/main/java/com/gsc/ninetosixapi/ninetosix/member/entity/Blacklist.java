package com.gsc.ninetosixapi.ninetosix.member.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Blacklist {

    @Id
    @Column()
    private String accessToken;
    private LocalDateTime insertDate;
    // private Long expiration;
    public static Blacklist createBlacklist(String accessToken) {
        return Blacklist.builder()
                .accessToken(accessToken)
                .insertDate(LocalDateTime.now())
                .build();
    }
}
