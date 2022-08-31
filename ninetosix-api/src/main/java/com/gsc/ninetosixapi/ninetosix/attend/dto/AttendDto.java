package com.gsc.ninetosixapi.ninetosix.attend.dto;

import com.gsc.ninetosixapi.ninetosix.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class AttendDto {
    private String email;
    private String userStatus;
    private Long companyLocationId;
}
