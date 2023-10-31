package com.gsc.ninetosixapi.ninetosix.signature.repository;

import com.gsc.ninetosixapi.ninetosix.signature.entity.Signature;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SignatureRepository extends JpaRepository<Signature, Long> {
    Optional<Signature> findByMemberId(Long memberId);
    void deleteByMemberId(Long memberId);
}
