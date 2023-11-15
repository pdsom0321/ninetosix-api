package com.gsc.ninetosixapi.core.interceptor;

import com.gsc.ninetosixapi.core.jwt.TokenConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {
    private final Environment environment;

    /**
     * Request Header에서 X-UUID, X-AUTH를 꺼내 유효성 검사(front를 통한 HTTP 통신인지 확인) 2023-11-14
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!(handler instanceof HandlerMethod)) {
            return true;
        }

        String validStr = environment.getProperty("auth.key");
        if (!isValidAccess(request, validStr)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden!!!!!!");
            return false;
        }

        return true;
    }

    /**
     * X-UUID 와 VALID_STR을 합친 문자열 = X-AUTH SHA256해싱하지 않은 평문
     * 따라서, X-AUTH 가 X-UUID 와 VALID_STR을 합친 문자열을 SHA256 해싱한 값과 매칭되는지 비교
     */
    private boolean isValidAccess(HttpServletRequest request, String validStr) {
        String uuidHeader = request.getHeader(TokenConfig.UUID_HEADER);
        String authHeader = request.getHeader(TokenConfig.AUTH_HEADER);
        log.info("uuidHeader: {}", uuidHeader);
        log.info("authHeader: {}", authHeader);

        return Objects.nonNull(uuidHeader) && Objects.nonNull(authHeader) && isValidAuth(uuidHeader, authHeader, validStr);
    }

    private boolean isValidAuth(String uuidHeader, String authHeader, String validStr) {
        String validString = String.join(uuidHeader, validStr);

        byte[] salt = generateSalt();
        byte[] hashedValidString = hashString(validString, salt);
        log.info("validString: {}", validString);

        return MessageDigest.isEqual(hashedValidString, hexStringToByteArray(authHeader));
    }

    private byte[] hashString(String input, byte[] salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(salt);
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

    private static byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return salt;
    }
}
