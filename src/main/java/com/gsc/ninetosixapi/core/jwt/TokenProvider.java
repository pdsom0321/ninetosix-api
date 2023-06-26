package com.gsc.ninetosixapi.core.jwt;

import com.gsc.ninetosixapi.ninetosix.member.repository.BlacklistRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TokenProvider {
    @Autowired
    private BlacklistRepository blacklistRepository;

    private final Key key;

    public TokenProvider(@Value("${jwt.secret}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String generateAccessToken(String email, Long memberId, long expireTime) {
        return Jwts.builder()
                .setSubject(email)                                          // payload "sub": "email"
                .claim(TokenConfig.AUTHORITIES_KEY, "ROLE_MEMBER")    // payload "auth": "ROLE_MEMBER"
                .claim(TokenConfig.MEMBER_ID, memberId)                     // payload "id" : 1
                .setExpiration(new Date(expireTime))                        // payload "exp": 1516239022 (예시)
                .signWith(key, SignatureAlgorithm.HS512)                    // header "alg": "HS512"
                .compact();
    }

    public String generateRefreshToken(long expireTime) {
        return Jwts.builder()
                .setExpiration(new Date(expireTime))
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get(TokenConfig.AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(TokenConfig.AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public Long getId(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);
        Long userId = claims.get("id", Long.class);

        return userId;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            if (blacklistRepository.existsByAccessToken(token)) {
                log.info("blacklist - 로그아웃된 사용자 입니다.");
                throw new JwtException("로그아웃된 사용자의 JWT 토큰입니다.");
            }
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
            throw new JwtException("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    /*public LoginResDTO reissue(TokenReqDTO reqDTO, Member member) {
        // 1. Refresh Token 검증
        if (!validateToken(reqDTO.getRefreshToken())) {
            log.error("REQ Refresh Token : " + reqDTO.getRefreshToken());
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = getAuthentication(reqDTO.getAccessToken());

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
        LoginResDTO loginResDTO = generateTokenDto(authentication, member);

        // 6. 저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(loginResDTO.refreshToken());
        refreshTokenRepository.save(newRefreshToken);

        // 토큰 발급
        return loginResDTO;
    }*/
}
