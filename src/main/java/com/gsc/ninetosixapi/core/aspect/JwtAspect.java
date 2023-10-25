package com.gsc.ninetosixapi.core.aspect;

import com.gsc.ninetosixapi.ninetosix.member.entity.Member;
import com.gsc.ninetosixapi.ninetosix.member.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.time.LocalDateTime;

@Aspect
@Component
public class JwtAspect {
    public static final String AUTHORIZATION_HEADER = "Authorization";

    public static final String BEARER_PREFIX = "Bearer ";

    private final Key key;

    private final MemberService memberService;

    public JwtAspect(@Value("${jwt.secret}") String secretKey, MemberService memberService) {
        this.memberService = memberService;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    @Around("@annotation(UserId)")
    public Object checkAccessToken(ProceedingJoinPoint joinPoint) throws Throwable {
        // 생성된 값을 HttpServletRequest에 저장
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            String token = request.getHeader(AUTHORIZATION_HEADER).substring(BEARER_PREFIX.length());
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            Long memberId = claims.get("id", Long.class);

            request.setAttribute("memberId", memberId);

            // 비밀번호 변경 기한 확인 로직 추가 2023-10-25
            Member member = memberService.findById(memberId);
            LocalDateTime passwordExpiryDate = member.getPasswordExpiryDate();
            if(passwordExpiryDate.isBefore(LocalDateTime.now())) {
                HttpServletResponse response = ((ServletRequestAttributes) requestAttributes).getResponse();
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("Password has expired.");

                return null; // 메서드 실행 중단
            }
        }

        return joinPoint.proceed();
    }
}
