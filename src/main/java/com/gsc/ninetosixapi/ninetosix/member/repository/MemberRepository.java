package com.gsc.ninetosixapi.ninetosix.member.repository;

import com.gsc.ninetosixapi.ninetosix.member.entity.Member;
import com.gsc.ninetosixapi.ninetosix.vo.YNCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    List<Member> findAllByTeamIdAndUseYnAndDeleteYn(long teamId, YNCode useYn, YNCode deleteYn);
    Boolean existsByEmail(String email);
}
