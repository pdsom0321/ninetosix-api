package com.gsc.ninetosixapi.core.jwt;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        if(authException instanceof BadCredentialsException) {
            // 유효한 자격증명을 제공하지 않고 비밀번호가 일치하지 않을 때 400
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "비밀번호가 일치하지 않음");
        } else if(authException instanceof InternalAuthenticationServiceException) {
            // 유효한 자격증명을 제공하지 않고 존재하지 않는 아이디일 때 400
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "존재하지 않는 아이디");
        } else {
            // 유효한 자격증명을 제공하지 않고 접근하려 할 때 401
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized!");
        }

    }
}
