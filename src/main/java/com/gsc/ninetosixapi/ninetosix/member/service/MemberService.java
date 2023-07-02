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
    private final BlacklistRepository blacklistRepository;
    private final CompanyService companyService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    public final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

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
        String accessToken = tokenProvider.generateToken(email, id, now+TokenConfig.ACCESS_TOKEN_EXPIRE_TIME);
        String refreshToken = tokenProvider.generateToken(email, id, now+ TokenConfig.REFRESH_TOKEN_EXPIRE_TIME);

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
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        // 3. DB에 저장된 Refresh Token 제거
        String email = authentication.getName();
        // Long memberId = MemberContext.getMemberId();
        // String email = findById(memberId).getEmail();
        refreshTokenRepository.deleteByEmail(email);

        // 4. Access Token blacklist에 등록하여 만료시키기
        blacklistRepository.save(Blacklist.create(accessToken));
    }

    public ReissueResDTO reissue(ReissueReqDTO reqDTO) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(reqDTO.refreshToken())) {
            log.error("REQ Refresh Token : " + reqDTO.refreshToken());
            throw new RuntimeException("Refresh Token validation false !!!!!!");
        }

        // 2. Access Token 에서 Member ID 가져오기
        // Authentication authentication = tokenProvider.getAuthentication(reqDTO.refreshToken());
        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        Long id = tokenProvider.getId(reqDTO.refreshToken());   // payload에서 userId 가져옴
        String email = memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException(""))
                .getEmail();  // 아직은 구현 전이라, id를 통해 email 가져옴
        long now = new Date().getTime();
        RefreshToken refreshTokenData = refreshTokenRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshTokenData.getToken().equals(reqDTO.refreshToken())) {
            log.error("DB Refresh Token : " + refreshTokenData.getToken());
            log.error("Request Refresh Token : " + reqDTO.refreshToken());
            log.error("equals ? : " + refreshTokenData.getToken().equals(reqDTO.refreshToken()));
            throw new RuntimeException("DB와 요청(Request)의 토큰이 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        String accessToken = tokenProvider.generateToken(email, id, now+TokenConfig.ACCESS_TOKEN_EXPIRE_TIME);
        String refreshToken = tokenProvider.generateToken(email, id, now+ TokenConfig.REFRESH_TOKEN_EXPIRE_TIME);

        // 6. 저장소 정보 업데이트
        refreshTokenData.updateToken(refreshToken);

        return ReissueResDTO.of(accessToken, refreshToken);
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
