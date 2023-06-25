package com.gsc.ninetosixapi.ninetosix.member.service;

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
import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    public final TokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final CompanyService companyService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final BlacklistRepository blacklistRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResDTO login(LoginReqDTO reqDTO) {
        // TODO : JWT토큰 관련 로직 따로 뺄것
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = reqDTO.toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성 (name 넘겨주기 위해 사용자 이름 가져오는 로직 추가 22.10.21)
        Member member = getMember(authentication.getName());
        LoginResDTO loginResDTO = tokenProvider.generateTokenDto(authentication, member);

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(loginResDTO.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        // 5. 토큰 발급
        return loginResDTO;
    }

    public ResponseEntity pwdChange(@NotNull String email, @NotNull PwdChangeReqDTO reqDTO) {
        Member member = getMember(email);
        String encodePassword = passwordEncoder.encode(reqDTO.getPassword());
        member.updatePassword(encodePassword);

        return new ResponseEntity(HttpStatus.OK);
    }

    public ResponseEntity logout(LogoutReqDTO reqDTO) {
        String accessToken = reqDTO.getAccessToken();
        log.info("accessToken : " + accessToken);

        // 1. Access Token 검증
        if (!tokenProvider.validateToken(accessToken)) {
            log.error("accessToken : " + accessToken);
            throw new RuntimeException("access Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 authentication 을 가져옴
        Authentication authentication = tokenProvider.getAuthentication(accessToken);

        // 3. DB에 저장된 Refresh Token 제거
        String email = authentication.getName();
        refreshTokenRepository.deleteByKey(email);

        // 4. Access Token blacklist에 등록하여 만료시키기
        // 해당 엑세스 토큰의 남은 유효시간을 얻음 tokenProvider.getExpiration -> insertDate로 변경 추후 배치 돌면서 데이터 삭제 예정
        // Long expiration = tokenProvider.getExpiration(accessToken);
        blacklistRepository.save(Blacklist.createBlacklist(accessToken));

        return new ResponseEntity(HttpStatus.OK);
    }

    public Member getMember(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException("존재하지 않는 계정 입니다."));
    }

    public Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    public List<Member> findMemberAll() {
        return memberRepository.findAll();
    }
}
