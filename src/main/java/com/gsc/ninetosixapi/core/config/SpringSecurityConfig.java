package com.gsc.ninetosixapi.core.config;

import com.gsc.ninetosixapi.core.jwt.JwtAccessDeniedHandler;
import com.gsc.ninetosixapi.core.jwt.JwtAuthenticationEntryPoint;
import com.gsc.ninetosixapi.core.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

//    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()

                // exception handling 할 때 우리가 만든 클래스를 추가
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .authorizeRequests()
                .antMatchers("/h2-console/**",
                        "/v2/api-docs/**",
                        "/swagger*/**",
                        "/webjars/**",
                        "/auth/**").permitAll()
                .antMatchers("/", "/**").permitAll() //.hasAuthority("ROLE_ADMIN")
                .anyRequest().authenticated()

                // h2-console 을 위한 설정을 추가
                .and()
                .headers()
                .frameOptions().sameOrigin()

                // 시큐리티는 기본적으로 세션을 사용
                // 여기서는 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // JwtFilter 를 addFilterBefore 로 등록했던 JwtSecurityConfig 클래스를 적용
                .and()
                .apply(new JwtSecurityConfig(tokenProvider));

                /*.and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/loginProc")
                .usernameParameter("id")
                .passwordParameter("pw")
                .defaultSuccessUrl("/main", true)
                .permitAll()
                .and()
                .userDetailsService(customUserDetailsService)
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logoutProc"));*/

        return http.build();
    }

    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web -> web.ignoring().antMatchers("/v2/api-docs/**",
                "/swagger-ui/**",
                "/images/**",
                "/js/**",
                "/css/**")
        );
    }
}
