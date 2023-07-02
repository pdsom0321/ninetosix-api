package com.gsc.ninetosixapi.ninetosix.member.controller;

import com.gsc.ninetosixapi.ninetosix.member.dto.GenerateCodeReqDTO;
import com.gsc.ninetosixapi.ninetosix.member.dto.VerifyCodeReqDTO;
import com.gsc.ninetosixapi.ninetosix.member.service.AuthenticationCodeService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationCodeController {
    private final AuthenticationCodeService authenticationCodeService;

    @ApiOperation(value = "회원가입 시 인증번호 발급")
    @PostMapping("/member/generate-code")
    public ResponseEntity<Void> generateCode(@RequestBody GenerateCodeReqDTO reqDTO) {
        authenticationCodeService.generateCode(reqDTO);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "회원가입 시 인증번호 검증")
    @PostMapping("/member/verify-code")
    public ResponseEntity<Boolean> verifyCode(@RequestBody VerifyCodeReqDTO reqDTO) {
        return ResponseEntity.ok(authenticationCodeService.verifyCode(reqDTO));
    }

}
