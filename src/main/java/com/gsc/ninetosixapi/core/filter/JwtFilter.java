package com.gsc.ninetosixapi.core.filter;

import com.gsc.ninetosixapi.core.jwt.TokenConfig;
import com.gsc.ninetosixapi.core.jwt.TokenProvider;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    @Value("${auth.key}")
    private String VALID_STRING;

    private final TokenProvider tokenProvider;

    // 실제 필터링 로직은 doFilterInternal 에 들어감
    // JWT 토큰의 인증 정보를 현재 쓰레드의 SecurityContext 에 저장하는 역할 수행
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 1. Request Header 에서 토큰을 꺼냄
        String jwt = resolveToken(request);

        try {
            // 2. validateToken 으로 토큰 유효성 검사
            // 정상 토큰이면 해당 토큰으로 Authentication 을 가져와서 SecurityContext 에 저장
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                log.info("================jwt: " + jwt);
                Authentication authentication = tokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // 3. Request Header에서 X-UUID, X-AUTH를 꺼내 유효성 검사(front를 통한 HTTP 통신인지 확인) 2023-11-14
                if (!isValidAccess(request))
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied.");

                // Long memberId = tokenProvider.getId(jwt);
                // MemberContext.setMemberId(memberId);
            }

            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            log.error("JwtException !!!!!!");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized!!");
        }
    }

    // Request Header 에서 토큰 정보를 꺼내오기
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(TokenConfig.AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(TokenConfig.BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * X-UUID 와 VALID_STR을 합친 문자열 = X-AUTH SHA256해싱하지 않은 평문
     * 따라서, X-AUTH 가 X-UUID 와 VALID_STR을 합친 문자열을 SHA256 해싱한 값과 매칭되는지 비교
     */
    private boolean isValidAccess(HttpServletRequest request) {
        String uuidHeader = request.getHeader(TokenConfig.UUID_HEADER);
        String authHeader = request.getHeader(TokenConfig.AUTH_HEADER);
        log.info("uuidHeader: {}", uuidHeader);
        log.info("authHeader: {}", authHeader);

        return Objects.nonNull(uuidHeader) && Objects.nonNull(authHeader) && isValidAuth(uuidHeader, authHeader);
    }

    private boolean isValidAuth(String uuidHeader, String authHeader) {
        String validString = String.join(uuidHeader, VALID_STRING);
        byte[] hashedValidString = hashString(validString);
        log.info("validString: {}", validString);

        return MessageDigest.isEqual(hashedValidString, hexStringToByteArray(authHeader));
    }

    private byte[] hashString(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(input.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    private byte[] hexStringToByteArray(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }
}
