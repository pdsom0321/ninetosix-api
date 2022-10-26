package com.gsc.ninetosixapi.ninetosix.member.dto;

import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
public class UserDetailsDTO implements UserDetails {

    private String email;
    private String pwd;
    private boolean isAccountNonExpired = true;
    private Set<GrantedAuthority> role = new HashSet<>();

    public UserDetailsDTO(String email, String pwd, Set<GrantedAuthority> role) {
        this.email = email;
        this.pwd = pwd;
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role;
    }

    @Override
    public String getPassword() {
        return pwd;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
