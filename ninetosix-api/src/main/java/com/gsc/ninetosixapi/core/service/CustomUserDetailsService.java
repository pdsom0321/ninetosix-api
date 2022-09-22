package com.gsc.ninetosixapi.core.service;

import com.gsc.ninetosixapi.ninetosix.member.dto.UserDetailsDTO;
import com.gsc.ninetosixapi.ninetosix.member.entity.Member;
import com.gsc.ninetosixapi.ninetosix.member.entity.MemberRole;
import com.gsc.ninetosixapi.ninetosix.member.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private MemberRepository memberRepository;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("############################ loadUserByUsername - username : " + username);
        return memberRepository.findByEmail(username)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));
    }

    private UserDetails createUserDetails(Member member) {
        return new UserDetailsDTO(
                member.getEmail(),
                member.getPassword(),
                this.converSimpleAuthorities(member.getRole())
        );
    }

    private Set<GrantedAuthority> converSimpleAuthorities(Set<MemberRole> roleList) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for(MemberRole item : roleList) {
            authorities.add(new SimpleGrantedAuthority(item.getRole()));
        }
        return authorities;
    }
}
