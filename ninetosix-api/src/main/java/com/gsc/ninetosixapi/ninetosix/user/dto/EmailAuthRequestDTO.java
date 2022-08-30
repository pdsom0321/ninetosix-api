package com.gsc.ninetosixapi.ninetosix.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailAuthRequestDTO {
    String email;
    String authToken;
}
