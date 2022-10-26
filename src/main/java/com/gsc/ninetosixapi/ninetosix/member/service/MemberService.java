package com.gsc.ninetosixapi.ninetosix.member.service;

import com.gsc.ninetosixapi.core.util.SecurityUtil;
import com.gsc.ninetosixapi.ninetosix.member.dto.MemberResDTO;
import com.gsc.ninetosixapi.ninetosix.member.entity.Member;
import com.gsc.ninetosixapi.ninetosix.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    public MemberResDTO getMemberInfo(String email) {
        return memberRepository.findByEmail(email)
                .map(MemberResDTO::of)
                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));
    }

    // 현재 SecurityContext 에 있는 유저 정보 가져오기
    public MemberResDTO getMyInfo() {
        return memberRepository.findByEmail(SecurityUtil.getCurrentUserId())
                .map(MemberResDTO::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));
    }

    public Member getMember(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow();
    }
}
