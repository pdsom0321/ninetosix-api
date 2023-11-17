package com.gsc.ninetosixapi.core.interceptor;

import com.gsc.ninetosixapi.core.jwt.TokenConfig;
import com.gsc.ninetosixapi.core.util.AESCrypto;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {
    private final Environment environment;

    /**
     * Request Header에서 X-UUID-1, X-UUID-2, X-UUID-3, X-AUTH를 꺼내 유효성 검사(front를 통한 HTTP 통신인지 확인)
     *  x-uuid-1 (UUID), x-uuid-2 (iv), x-uuid-3 (yyyymmddhhmmss AES 암호화)
     *  x-auth (UUID + key SHA256 암호화)
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String validStr = environment.getProperty("auth.key");

        if (!isValidTime(request) || !isValidAccess(request, validStr)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden!!!!!!");
            return false;
        }

        return true;
    }

    /**
     * X-UUID-1 와 VALID_STR(auth.key)을 합친 문자열 = X-AUTH SHA256해싱하지 않은 평문
     * 따라서, X-AUTH 가 X-UUID-1 와 VALID_STR을 합친 문자열을 SHA256 해싱한 값과 매칭되는지 비교
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
        byte[] hashedValidString = hashString(validString);

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

    /**
     * X-UUID-3 (dayHeader) AES 복호화하여 1분 이내인지 확인
     */
    private boolean isValidTime(HttpServletRequest request) throws Exception {
        String aesKey = environment.getProperty("aes.key");
        String dayHeader = request.getHeader(TokenConfig.DAY_HEADER);
        String ivHeader = request.getHeader(TokenConfig.IV_HEADER);
        log.info("dayHeader: {}", dayHeader);
        log.info("ivHeader: {}", ivHeader);

        String encodedValue = new AESCrypto(aesKey, 16).aesDecode(dayHeader, ivHeader);
        log.info("encodedValue: {}", encodedValue);

        // 현재 시간 구하기
        Date now = new Date();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date encodedDate;
        try {
            encodedDate = dateFormat.parse(encodedValue);

            long diffInMilliseconds = now.getTime() - encodedDate.getTime();
            long diffInMinutes = Math.abs(diffInMilliseconds / (60 * 1000));
            log.info("diffInMinutes: {}", diffInMinutes);
            if (diffInMinutes > 1) {
                log.info("1분 이상 차이 !!!!!!!!!!!!!!!");
                return false;
            }
        } catch (ParseException e) {
            log.error("날짜 형식이 올바르지 않습니다.");
        }

        return true;
    }
}
