package com.gsc.ninetosixapi.ninetosix.user.repository;

import com.gsc.ninetosixapi.ninetosix.user.entity.AuthCode;
import com.gsc.ninetosixapi.ninetosix.user.vo.AuthCodeFrom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AuthCodeRepository extends JpaRepository<AuthCode, Long> {
    Optional<AuthCode> findByEmailAndRanCodeAndFromTypeAndExpireDateGreaterThanAndExpired(String email, String ranCode, AuthCodeFrom from, LocalDateTime currentTime, Boolean expired);
}
