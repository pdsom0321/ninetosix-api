package com.gsc.ninetosixapi.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {

    private final CustomUserDetailService customUserDetailService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/h2-console/**",
                        "/v2/api-docs/**",
                        "/swagger*/**",
                        "/webjars/**",
                        "/login").permitAll()
                .antMatchers("/").hasAuthority("ROLE_ADMIN")
                .anyRequest().authenticated()
                .and()
                .headers()
                .frameOptions().sameOrigin()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/loginProc")
                .usernameParameter("id")
                .passwordParameter("pw")
                .defaultSuccessUrl("/main", true)
                .permitAll()
                .and()
                .userDetailsService(customUserDetailService)
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logoutProc"));

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
