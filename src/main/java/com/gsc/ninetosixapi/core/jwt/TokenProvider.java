package com.gsc.ninetosixapi.core.jwt;

import com.gsc.ninetosixapi.ninetosix.member.repository.BlacklistRepository;
import com.gsc.ninetosixapi.ninetosix.member.vo.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
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
    private final BlacklistRepository blacklistRepository;

    private final Key key;

    public TokenProvider(BlacklistRepository blacklistRepository, @Value("${jwt.secret}") String secretKey) {
        this.blacklistRepository = blacklistRepository;
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String generateToken(String email, Long memberId, long expireTime) {
        return Jwts.builder()
                .setSubject(email)                                          // payload "sub": "email"
                .claim(TokenConfig.AUTHORITIES_KEY, Role.ROLE_MEMBER)       // payload "auth": "ROLE_MEMBER"
                .claim(TokenConfig.MEMBER_ID, memberId)                     // payload "id" : 1
                .setExpiration(new Date(expireTime))                        // payload "exp": 1516239022 (예시)
                .signWith(key, SignatureAlgorithm.HS512)                    // header "alg": "HS512"
                .compact();
    }

    public Authentication getAuthentication(String token) {
        // 토큰 복호화
        Claims claims = parseClaims(token);

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

    private Claims parseClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public long getId(String token) {
        return parseClaims(token).get("id", Long.class);
    }

    public String getEmail(String token) {
        return parseClaims(token).getSubject();
    }
}
