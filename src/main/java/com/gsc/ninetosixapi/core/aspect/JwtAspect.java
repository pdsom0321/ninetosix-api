package com.gsc.ninetosixapi.core.aspect;

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
import java.security.Key;

@Aspect
@Component
public class JwtAspect {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private final Key key;

    public JwtAspect(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    @Around("@annotation(UserId)")
    public Object checkAccessToken(ProceedingJoinPoint joinPoint) throws Throwable {
        // 생성된 값을 HttpServletRequest에 저장
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            String token = request.getHeader(AUTHORIZATION_HEADER).replace(BEARER_PREFIX, "");
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
            Long memberId = claims.get("id", Long.class);

            request.setAttribute("memberId", memberId);
        }

        return joinPoint.proceed();
    }

    /*@Pointcut("@annotation(com.gsc.ninetosixapi.core.aspect.UserId)")
    public void userIdPointcut() {
        System.out.println("!!!!!!!!!!!!!!!!!!!!");
    }*/

    /*@Before("userIdPointcut() && args(.., request)")
    public void beforeMethodExecution(JoinPoint joinPoint, HttpServletRequest request) {
        System.out.println("!!!!!!!!!!!!!!!!!!!!");
        String token = request.getHeader(AUTHORIZATION_HEADER).replace(BEARER_PREFIX, "");
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        Long userId = claims.get("id", Long.class);

        System.out.println("@@ userId = " + userId);

        // Aspect 클래스에서 가져온 "userid" 값을 저장해두는 방법은 여러 가지가 있습니다.
        // 예를 들어, ThreadLocal을 사용하여 현재 스레드에 저장할 수 있습니다.
        // 현재 스레드의 "userid" 값을 저장하기 위해 UserContext 클래스를 작성합니다.
        UserContext.setUserId(userId);
    }*/
}
