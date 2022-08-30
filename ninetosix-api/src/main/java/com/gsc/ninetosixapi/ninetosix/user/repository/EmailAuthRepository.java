package com.gsc.ninetosixapi.ninetosix.user.repository;

import com.gsc.ninetosixapi.ninetosix.user.entity.EmailAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EmailAuthRepository extends JpaRepository<EmailAuth, Long> {
    Optional<EmailAuth> findByEmailAndAuthTokenAndExpireDateGreaterThanAndExpired(String email, String authToken, LocalDateTime currentTime, Boolean expired);
}
