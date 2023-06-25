package com.gsc.ninetosixapi.ninetosix.member.controller;

import com.gsc.ninetosixapi.core.jwt.TokenProvider;
import com.gsc.ninetosixapi.ninetosix.member.dto.*;
import com.gsc.ninetosixapi.ninetosix.member.service.AuthService;
import com.gsc.ninetosixapi.ninetosix.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final TokenProvider tokenProvider;
    private final MemberService memberService;

    @PostMapping("/auth")
    public ResponseEntity<LoginResDTO> login(@RequestBody LoginReqDTO reqDTO) {
        return ResponseEntity.ok(authService.login(reqDTO));
    }

    @PutMapping("/auth/pwd")
    public ResponseEntity pwdChange(@ApiIgnore Principal principal, @RequestBody PwdChangeReqDTO reqDTO) {
        return ResponseEntity.ok(authService.pwdChange(principal.getName(), reqDTO));
    }

    @PostMapping("/reissue")
    public ResponseEntity reissue(@ApiIgnore Principal principal, @RequestBody TokenReqDTO reqDTO) {
        return ResponseEntity.ok(tokenProvider.reissue(reqDTO, memberService.getMember(principal.getName())));
    }

    @PostMapping("/auth/out")
    public ResponseEntity logout(@RequestBody LogoutReqDTO reqDTO) {
        return ResponseEntity.ok(authService.logout(reqDTO));
    }

}
