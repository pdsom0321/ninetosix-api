package com.gsc.ninetosixapi.ninetosix.user.controller;

import com.gsc.ninetosixapi.ninetosix.user.dto.*;
import com.gsc.ninetosixapi.ninetosix.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/auth/new")
    public ResponseEntity<UserResDTO> signup(@RequestBody signupReqDTO reqDTO) {
        return ResponseEntity.ok(authService.signup(reqDTO));
    }

    @PostMapping("/auth")
    public ResponseEntity<TokenDTO> login(@RequestBody LoginReqDTO reqDTO) {
        return ResponseEntity.ok(authService.login(reqDTO));
    }

    @PutMapping("/auth/pwd")
    public ResponseEntity pwdChange(@RequestBody PwdChangeReqDTO reqDTO) {
        return ResponseEntity.ok(authService.pwdChange(reqDTO));
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDTO> reissue(@RequestBody TokenReqDTO reqDTO) {
        return ResponseEntity.ok(authService.reissue(reqDTO));
    }

}
