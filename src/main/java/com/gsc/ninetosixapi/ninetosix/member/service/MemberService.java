package com.gsc.ninetosixapi.ninetosix.member.service;

import com.gsc.ninetosixapi.core.jwt.TokenProvider;
import com.gsc.ninetosixapi.ninetosix.company.entity.Company;
import com.gsc.ninetosixapi.ninetosix.company.service.CompanyService;
import com.gsc.ninetosixapi.ninetosix.member.dto.LoginReqDTO;
import com.gsc.ninetosixapi.ninetosix.member.dto.LoginResDTO;
import com.gsc.ninetosixapi.ninetosix.member.dto.SignupReqDTO;
import com.gsc.ninetosixapi.ninetosix.member.dto.SignupResDTO;
import com.gsc.ninetosixapi.ninetosix.member.entity.Member;
import com.gsc.ninetosixapi.ninetosix.member.entity.MemberRole;
import com.gsc.ninetosixapi.ninetosix.member.entity.RefreshToken;
import com.gsc.ninetosixapi.ninetosix.member.repository.MemberRepository;
import com.gsc.ninetosixapi.ninetosix.member.repository.MemberRoleRepository;
import com.gsc.ninetosixapi.ninetosix.member.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
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
    private final RefreshTokenRepository refreshTokenRepository;
    private final CompanyService companyService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    public final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    public LoginResDTO login(LoginReqDTO reqDTO) {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = toAuthentication(reqDTO);

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성 (name 넘겨주기 위해 사용자 이름 가져오는 로직 추가 22.10.21)
        Member member = getMemberByEmail(authentication.getName());
        LoginResDTO loginResDTO = tokenProvider.generateToken(authentication, member);

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(loginResDTO.refreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        // 5. 토큰 발급
        return loginResDTO;
    }

    public SignupResDTO signup(SignupReqDTO reqDTO) {
        memberRepository.existsByEmail(reqDTO.email()).orElseThrow(() -> new RuntimeException("이미 가입한 계정이 있습니다."));

        String encodePassword = passwordEncoder.encode(reqDTO.password());
        Company company = companyService.getCompany(reqDTO.companyCode());

        Member member = memberRepository.save(Member.create(reqDTO, encodePassword, company));
        memberRoleRepository.save(MemberRole.create(member));

        return SignupResDTO.of(member);
    }

    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("존재하지 않는 계정 입니다."));
    }

    public UsernamePasswordAuthenticationToken toAuthentication(LoginReqDTO reqDTO) {
        return new UsernamePasswordAuthenticationToken(reqDTO.email(), reqDTO.password());
    }
}
