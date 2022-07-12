package com.gsc.ninetosixapi.config.security;

import com.gsc.ninetosixapi.user.dto.UserDetailsDTO;
import com.gsc.ninetosixapi.user.entity.User;
import com.gsc.ninetosixapi.user.entity.UserRole;
import com.gsc.ninetosixapi.user.repositories.UserRepository;
import lombok.AllArgsConstructor;
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

@Service
@AllArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(username);

        if(user == null) throw new UsernameNotFoundException("User Not Found");

        return new UserDetailsDTO(
                user.getEmail(),
                user.getPwd(),
                this.converSimpleAuthorities(user.getRole())
        );
    }

    private Set<GrantedAuthority> converSimpleAuthorities(Set<UserRole> roleList) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        for(UserRole item : roleList) {
            authorities.add(new SimpleGrantedAuthority(item.getRole()));
        }
        return authorities;
    }
}
