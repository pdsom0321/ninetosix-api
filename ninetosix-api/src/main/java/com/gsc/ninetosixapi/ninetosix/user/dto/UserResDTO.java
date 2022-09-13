package com.gsc.ninetosixapi.ninetosix.user.dto;

import com.gsc.ninetosixapi.ninetosix.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserResDTO {
    private String email;
    private String name;

    public static UserResDTO of(User user) {
        return new UserResDTO(user.getEmail(), user.getName());
    }
}
