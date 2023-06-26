package com.gsc.ninetosixapi.ninetosix.member.service;

import com.gsc.ninetosixapi.core.jwt.MemberContext;
import com.gsc.ninetosixapi.core.jwt.TokenConfig;
import com.gsc.ninetosixapi.core.jwt.TokenProvider;
import com.gsc.ninetosixapi.ninetosix.company.entity.Company;
import com.gsc.ninetosixapi.ninetosix.company.service.CompanyService;
import com.gsc.ninetosixapi.ninetosix.member.dto.*;
import com.gsc.ninetosixapi.ninetosix.member.entity.Blacklist;
import com.gsc.ninetosixapi.ninetosix.member.entity.Member;
import com.gsc.ninetosixapi.ninetosix.member.entity.MemberRole;
import com.gsc.ninetosixapi.ninetosix.member.entity.RefreshToken;
import com.gsc.ninetosixapi.ninetosix.member.repository.BlacklistRepository;
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

import java.util.Date;
import java.util.List;
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
    private final BlacklistRepository blacklistRepository;

    public LoginResDTO login(LoginReqDTO reqDTO) {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = toAuthentication(reqDTO);

        // 2. authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨. 실제로 검증 (사용자 비밀번호 체크)하는 부분
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        String email = authentication.getName();
        Member member = findByEmail(email);
        long id = member.getId();
        long now = new Date().getTime();
        String accessToken = tokenProvider.generateAccessToken(email, id, now+TokenConfig.ACCESS_TOKEN_EXPIRE_TIME);
        String refreshToken = tokenProvider.generateRefreshToken(now+ TokenConfig.REFRESH_TOKEN_EXPIRE_TIME);

        // 4. RefreshToken 저장
        refreshTokenRepository.save(RefreshToken.create(email, refreshToken));

        return LoginResDTO.builder()
                .grantType(TokenConfig.BEARER_PREFIX)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .id(id)
                .name(member.getName())
                .build();
    }

    public SignupResDTO signup(SignupReqDTO reqDTO) {
        memberRepository.existsByEmail(reqDTO.email()).orElseThrow(() -> new RuntimeException("이미 가입한 계정이 있습니다."));

        String encodePassword = passwordEncoder.encode(reqDTO.password());
        Company company = companyService.getCompany(reqDTO.companyCode());

        Member member = memberRepository.save(Member.create(reqDTO, encodePassword, company));
        memberRoleRepository.save(MemberRole.create(member));

        return SignupResDTO.of(member);
    }

    public void changePassword(PasswordReqDTO reqDTO) {
        Long memberId = MemberContext.getMemberId();
        Member member = findById(memberId);

        String encodePassword = passwordEncoder.encode(reqDTO.password());
        member.updatePassword(encodePassword);
    }

    public void logout(LogoutReqDTO reqDTO) {
        String accessToken = reqDTO.accessToken();
        log.info("accessToken : " + accessToken);

        // 1. Access Token 검증
        if (!tokenProvider.validateToken(accessToken)) {
            throw new RuntimeException("access Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 authentication 을 가져옴
        // Authentication authentication = tokenProvider.getAuthentication(accessToken);
        // 3. DB에 저장된 Refresh Token 제거
        // String email = authentication.getName();
        Long memberId = MemberContext.getMemberId();
        String email = findById(memberId).getEmail();
        refreshTokenRepository.deleteByKey(email);

        // 4. Access Token blacklist에 등록하여 만료시키기
        blacklistRepository.save(Blacklist.create(accessToken));
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("존재하지 않는 계정 입니다."));
    }

    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public UsernamePasswordAuthenticationToken toAuthentication(LoginReqDTO reqDTO) {
        return new UsernamePasswordAuthenticationToken(reqDTO.email(), reqDTO.password());
    }
}
