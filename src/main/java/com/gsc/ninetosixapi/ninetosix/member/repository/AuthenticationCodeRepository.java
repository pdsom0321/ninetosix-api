package com.gsc.ninetosixapi.ninetosix.member.repository;

import com.gsc.ninetosixapi.ninetosix.member.entity.AuthenticationCode;
import com.gsc.ninetosixapi.ninetosix.member.vo.AuthenticationCodeType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AuthenticationCodeRepository extends JpaRepository<AuthenticationCode, Long> {
    Optional<AuthenticationCode> findByCodeAndEmailAndTypeAndExpiryDateGreaterThanAndIsCodeEntered(int code, String email, AuthenticationCodeType type, LocalDateTime currentTime, Boolean isCodeEntered);
}
