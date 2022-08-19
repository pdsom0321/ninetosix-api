package com.gsc.ninetosixapi.ninetosix.user.dto;

import com.gsc.ninetosixapi.ninetosix.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {
    private String email;

    public static UserResponseDTO of(User user) {
        return new UserResponseDTO(user.getEmail());
    }
}
