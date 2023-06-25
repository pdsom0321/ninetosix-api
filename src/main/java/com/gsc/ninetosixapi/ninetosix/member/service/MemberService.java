package com.gsc.ninetosixapi.ninetosix.member.service;

import com.gsc.ninetosixapi.ninetosix.company.entity.Company;
import com.gsc.ninetosixapi.ninetosix.company.service.CompanyService;
import com.gsc.ninetosixapi.ninetosix.member.dto.SignupReqDTO;
import com.gsc.ninetosixapi.ninetosix.member.dto.SignupResDTO;
import com.gsc.ninetosixapi.ninetosix.member.entity.Member;
import com.gsc.ninetosixapi.ninetosix.member.entity.MemberRole;
import com.gsc.ninetosixapi.ninetosix.member.repository.MemberRepository;
import com.gsc.ninetosixapi.ninetosix.member.repository.MemberRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final CompanyService companyService;
    private final PasswordEncoder passwordEncoder;

    public SignupResDTO signup(SignupReqDTO reqDTO) {
        memberRepository.existsByEmail(reqDTO.email()).orElseThrow(() -> new RuntimeException("이미 가입한 계정이 있습니다."));

        String encodePassword = passwordEncoder.encode(reqDTO.password());
        Company company = companyService.getCompany(reqDTO.companyCode());
        Member member = memberRepository.save(Member.createUser(reqDTO, encodePassword, company));

        memberRoleRepository.save(MemberRole.createUserRole(member));

        return SignupResDTO.of(member);
    }

    public Member getMember(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("존재하지 않는 계정 입니다."));
    }
}
