package com.gsc.ninetosixapi.ninetosix.member.repository;

import com.gsc.ninetosixapi.ninetosix.member.entity.Blacklist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistRepository extends JpaRepository<Blacklist, Long> {
    boolean existsByAccessToken(String accessToken);
}
