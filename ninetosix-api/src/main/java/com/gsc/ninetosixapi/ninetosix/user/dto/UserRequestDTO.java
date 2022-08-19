package com.gsc.ninetosixapi.ninetosix.user.dto;

import com.gsc.ninetosixapi.ninetosix.user.entity.User;
import com.gsc.ninetosixapi.ninetosix.user.entity.UserRole;
import com.gsc.ninetosixapi.ninetosix.user.vo.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {

    private String email;
    private String password;

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}
