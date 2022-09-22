package com.gsc.ninetosixapi.ninetosix.member.controller;

import com.gsc.ninetosixapi.ninetosix.member.dto.*;
import com.gsc.ninetosixapi.ninetosix.member.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/auth/new")
    public ResponseEntity<MemberResDTO> signup(@RequestBody signupReqDTO reqDTO) {
        return ResponseEntity.ok(authService.signup(reqDTO));
    }

    @PostMapping("/auth")
    public ResponseEntity<LoginResDTO> login(@RequestBody LoginReqDTO reqDTO) {
        return ResponseEntity.ok(authService.login(reqDTO));
    }

    @PutMapping("/auth/pwd")
    public ResponseEntity pwdChange(@RequestBody PwdChangeReqDTO reqDTO) {
        return ResponseEntity.ok(authService.pwdChange(reqDTO));
    }

    @PostMapping("/reissue")
    public ResponseEntity reissue(@RequestBody TokenReqDTO reqDTO) {
        return ResponseEntity.ok(authService.reissue(reqDTO));
    }

}
