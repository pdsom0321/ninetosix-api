package com.gsc.ninetosixapi.ninetosix.member.dto;

public record PasswordReqDTO(String email, String oldPassword, String newPassword) {
}
