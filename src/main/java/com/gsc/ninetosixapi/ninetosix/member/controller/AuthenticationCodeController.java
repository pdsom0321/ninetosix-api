package com.gsc.ninetosixapi.ninetosix.member.controller;

import com.gsc.ninetosixapi.ninetosix.member.dto.GenerateCodeReqDTO;
import com.gsc.ninetosixapi.ninetosix.member.dto.VerifyCodeReqDTO;
import com.gsc.ninetosixapi.ninetosix.member.service.AuthenticationCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationCodeController {
    private final AuthenticationCodeService authenticationCodeService;

    @PostMapping("/member/generate-code")
    public ResponseEntity<Void> generateCode(@RequestBody GenerateCodeReqDTO reqDTO) {
        authenticationCodeService.generateCode(reqDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/member/verify-code")
    public ResponseEntity<Boolean> verifyCode(@RequestBody VerifyCodeReqDTO reqDTO) {
        return ResponseEntity.ok(authenticationCodeService.verifyCode(reqDTO));
    }

}
