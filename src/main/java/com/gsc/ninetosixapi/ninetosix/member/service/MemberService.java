package com.gsc.ninetosixapi.ninetosix.member.service;

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
import com.gsc.ninetosixapi.ninetosix.team.entity.Team;
import com.gsc.ninetosixapi.ninetosix.team.service.TeamService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
    private final TeamService teamService;
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
        refreshTokenRepository.save(RefreshToken.create(email, refreshToken, now+TokenConfig.REFRESH_TOKEN_EXPIRE_TIME));

        // 5. 비밀번호 만료 확인 (PASSWORD_EXPIRY_DAY = 90)
        boolean isPasswordExpired = member.getPasswordExpiryDate().isBefore(LocalDateTime.now());

        return LoginResDTO.builder()
                .grantType(TokenConfig.BEARER_PREFIX)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .id(id)
                .name(member.getName())
                .isPasswordExpired(isPasswordExpired)
                .build();
    }

    public SignupResDTO signup(SignupReqDTO reqDTO) {
        if(memberRepository.existsByEmail(reqDTO.email())){
            throw new RuntimeException("이미 가입한 계정이 있습니다.");
        }

        String encodePassword = passwordEncoder.encode(reqDTO.password());
        Company company = companyService.getCompany(reqDTO.companyId());
        Team team = teamService.getTeam(reqDTO.teamId());

        Member member = memberRepository.save(Member.create(reqDTO, encodePassword, company, team));
        memberRoleRepository.save(MemberRole.create(member));

        return SignupResDTO.of(member);
    }

    public void changePassword(PasswordReqDTO reqDTO, long id) {
        Member member = findById(id);

        String encodePassword = passwordEncoder.encode(reqDTO.password());
        member.updatePassword(encodePassword);
    }

    public void changePasswordExpiry(long id) {
        Member member = findById(id);

        member.updatePasswordExpiry();
    }

    public void logout(LogoutReqDTO reqDTO) {
        String accessToken = reqDTO.accessToken();
        log.info("accessToken : " + accessToken);

        // 1. Access Token 검증
        if (!tokenProvider.validateToken(accessToken)) {
            throw new JwtException("Access Token 이 잘못되었습니다.");
        }

        // 2. Access Token 에서 authentication 을 가져옴
        Authentication authentication = tokenProvider.getAuthentication(accessToken);
        String email = authentication.getName();

        // 3. DB에 저장된 Refresh Token 제거
        refreshTokenRepository.deleteByEmail(email);

        // 4. Access Token blacklist에 등록하여 만료시키기
        blacklistRepository.save(Blacklist.create(accessToken));
    }

    public void withdrawal(long id) {
        memberRepository.deleteById(id);
    }

    public ReissueResDTO reissue(ReissueReqDTO reqDTO) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(reqDTO.refreshToken())) {
            log.error("REQ Refresh Token : " + reqDTO.refreshToken());
            throw new JwtException("Refresh Token 이 잘못되었습니다.");
        }

        // 2. Refresh Token 값으로 subject(email)값 가져옴
        String email = tokenProvider.getEmail(reqDTO.refreshToken());

        // 3. refresh token 만료되었거나 이미 로그아웃한 사용자인지 확인
        RefreshToken refreshTokenData = refreshTokenRepository.findByEmailAndExpiryDateGreaterThan(email, new Date())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshTokenData.getToken().equals(reqDTO.refreshToken())) {
            log.error("DB Refresh Token : " + refreshTokenData.getToken());
            log.error("Request Refresh Token : " + reqDTO.refreshToken());
            log.error("equals ? : " + refreshTokenData.getToken().equals(reqDTO.refreshToken()));
            throw new RuntimeException("DB와 ReqDTO의 Refresh Token 값이 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        Long id = tokenProvider.getId(reqDTO.refreshToken());
        long now = new Date().getTime();
        String accessToken = tokenProvider.generateToken(email, id, now+TokenConfig.ACCESS_TOKEN_EXPIRE_TIME);
        String refreshToken = tokenProvider.generateToken(email, id, now+ TokenConfig.REFRESH_TOKEN_EXPIRE_TIME);

        // 6. 저장소 정보 업데이트
        refreshTokenData.updateToken(refreshToken, now+ TokenConfig.REFRESH_TOKEN_EXPIRE_TIME);

        return ReissueResDTO.of(accessToken, refreshToken);
    }

    public MyPageResDTO myPage(long id) {
        Member member = findById(id);

        return MyPageResDTO.of(member);
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("존재하지 않는 계정 입니다."));
    }

    public Member findById(Long id) {
        return memberRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public List<Member> findAllByTeamId(Long teamId) {
        return memberRepository.findAllByTeamId(teamId);
    }

    public UsernamePasswordAuthenticationToken toAuthentication(LoginReqDTO reqDTO) {
        return new UsernamePasswordAuthenticationToken(reqDTO.email(), reqDTO.password());
    }
}
