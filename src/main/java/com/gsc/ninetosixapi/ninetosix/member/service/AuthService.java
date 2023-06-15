package com.gsc.ninetosixapi.ninetosix.member.service;

import com.gsc.ninetosixapi.core.jwt.TokenProvider;
import com.gsc.ninetosixapi.ninetosix.company.entity.Company;
import com.gsc.ninetosixapi.ninetosix.company.service.CompanyService;
import com.gsc.ninetosixapi.ninetosix.member.dto.*;
import com.gsc.ninetosixapi.ninetosix.member.entity.Blacklist;
import com.gsc.ninetosixapi.ninetosix.member.entity.RefreshToken;
import com.gsc.ninetosixapi.ninetosix.member.entity.Member;
import com.gsc.ninetosixapi.ninetosix.member.entity.MemberRole;
import com.gsc.ninetosixapi.ninetosix.member.repository.BlacklistRepository;
import com.gsc.ninetosixapi.ninetosix.member.repository.RefreshTokenRepository;
import com.gsc.ninetosixapi.ninetosix.member.repository.MemberRepository;
import com.gsc.ninetosixapi.ninetosix.member.repository.MemberRoleRepository;
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

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final MemberRepository memberRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final CompanyService companyService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    private final BlacklistRepository blacklistRepository;

    private final PasswordEncoder passwordEncoder;

    public MemberResDTO signup(SignupReqDTO signupReqDTO) {
        if (memberRepository.existsByEmail(signupReqDTO.getEmail())) {
            throw new RuntimeException("이미 가입한 사용자 입니다.");
        }

        Company company = companyService.getCompany(signupReqDTO.getCompanyCode());
        Member member = memberRepository.save(Member.createUser(signupReqDTO, company, passwordEncoder));
        memberRoleRepository.save(MemberRole.createUserRole(member));

        return MemberResDTO.of(member);
    }

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
        member.updatePassword(reqDTO.getPassword(), passwordEncoder);

        return new ResponseEntity(HttpStatus.OK);
    }

    public LoginResDTO reissue(TokenReqDTO reqDTO) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(reqDTO.getRefreshToken())) {
            log.error("REQ Refresh Token : " + reqDTO.getRefreshToken());
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(reqDTO.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(reqDTO.getRefreshToken())) {
            log.error("DB Refresh Token : " + refreshToken.getValue());
            log.error("REQ Refresh Token : " + reqDTO.getRefreshToken());
            log.error("equals ? : " + refreshToken.getValue().equals(reqDTO.getRefreshToken()));
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성 (name 넘겨주기 위해 사용자 이름 가져오는 로직 추가 22.10.21)
        Member member = getMember(authentication.getName());
        LoginResDTO loginResDTO = tokenProvider.generateTokenDto(authentication, member);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(loginResDTO.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return loginResDTO;
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
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));

        return member;
    }

    public Member findMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }
}
