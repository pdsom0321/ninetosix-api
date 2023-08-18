package com.gsc.ninetosixapi.ninetosix.member.repository;

import com.gsc.ninetosixapi.ninetosix.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    List<Member> findAllByTeamId(Long teamId);
    Boolean existsByEmail(String email);
}
