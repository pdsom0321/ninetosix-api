package com.gsc.ninetosixapi.ninetosix.user.service;

import com.gsc.ninetosixapi.core.jwt.TokenProvider;
import com.gsc.ninetosixapi.ninetosix.company.entity.Company;
import com.gsc.ninetosixapi.ninetosix.company.service.CompanyService;
import com.gsc.ninetosixapi.ninetosix.user.dto.*;
import com.gsc.ninetosixapi.ninetosix.user.entity.EmailAuth;
import com.gsc.ninetosixapi.ninetosix.user.entity.RefreshToken;
import com.gsc.ninetosixapi.ninetosix.user.entity.User;
import com.gsc.ninetosixapi.ninetosix.user.entity.UserRole;
import com.gsc.ninetosixapi.ninetosix.user.repository.RefreshTokenRepository;
import com.gsc.ninetosixapi.ninetosix.user.repository.UserRepository;
import com.gsc.ninetosixapi.ninetosix.user.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final CompanyService companyService;

    private final EmailAuthService emailAuthService;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public UserResponseDTO signup(UserInfoDTO userInfoDTO) {
        if (userRepository.existsByEmail(userInfoDTO.getEmail())) {
            throw new RuntimeException("이미 가입한 사용자 입니다.");
        }

        EmailAuth emailAuth = emailAuthService.save(userInfoDTO);

        Company company = companyService.getCompany(userInfoDTO.getCompanyCode());
        User user = userRepository.save(User.createUser(userInfoDTO, company, passwordEncoder, false));
        userRoleRepository.save(UserRole.createUserRole(user));

        // 이메일 인증
        emailAuthService.send(emailAuth.getEmail(), emailAuth.getAuthToken());

        return UserResponseDTO.of(user);
    }

    public TokenDTO login(UserRequestDTO userRequestDTO) {

        User user = userRepository.findByEmail(userRequestDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));

        if (!user.getEmailAuth())
            throw new RuntimeException("이메일 인증이 필요합니다.");

        // TODO : JWT토큰 관련 로직 따로 뺄것
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = userRequestDTO.toAuthentication();

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDTO tokenDTO = tokenProvider.generateTokenDto(authentication);

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDTO.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        // 5. 토큰 발급
        return tokenDTO;
    }

    public TokenDTO reissue(TokenRequestDTO tokenRequestDto) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. 저장소에서 Member ID 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4. Refresh Token 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5. 새로운 토큰 생성
        TokenDTO tokenDTO = tokenProvider.generateTokenDto(authentication);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDTO.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return tokenDTO;
    }
}
