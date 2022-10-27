package com.gsc.ninetosixapi.ninetosix.member.controller;

import com.gsc.ninetosixapi.ninetosix.member.dto.*;
import com.gsc.ninetosixapi.ninetosix.member.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/auth/new")
    public ResponseEntity<MemberResDTO> signup(@RequestBody SignupReqDTO reqDTO) {
        return ResponseEntity.ok(authService.signup(reqDTO));
    }

    @PostMapping("/auth")
    public ResponseEntity<LoginResDTO> login(@RequestBody LoginReqDTO reqDTO) {
        return ResponseEntity.ok(authService.login(reqDTO));
    }

    @PutMapping("/auth/pwd")
    public ResponseEntity pwdChange(@ApiIgnore Principal principal, @RequestBody PwdChangeReqDTO reqDTO) {
        return ResponseEntity.ok(authService.pwdChange(principal.getName(), reqDTO));
    }

    @PostMapping("/reissue")
    public ResponseEntity reissue(@RequestBody TokenReqDTO reqDTO) {
        return ResponseEntity.ok(authService.reissue(reqDTO));
    }

    @PostMapping("/auth/out")
    public ResponseEntity logout(@RequestBody LogoutReqDTO reqDTO) {
        return ResponseEntity.ok(authService.logout(reqDTO));
    }

}
