package com.gsc.ninetosixapi.ninetosix.member.controller;

import com.gsc.ninetosixapi.ninetosix.member.dto.LoginReqDTO;
import com.gsc.ninetosixapi.ninetosix.member.dto.LoginResDTO;
import com.gsc.ninetosixapi.ninetosix.member.dto.SignupReqDTO;
import com.gsc.ninetosixapi.ninetosix.member.dto.SignupResDTO;
import com.gsc.ninetosixapi.ninetosix.member.service.MemberService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @ApiOperation(value = "로그인", notes = "")
    @PostMapping("member")
    public ResponseEntity<LoginResDTO> login(@RequestBody LoginReqDTO reqDTO) {
        return ResponseEntity.ok(memberService.login(reqDTO));
    }

    @ApiOperation(value = "회원가입")
    @PostMapping("member/new")
    public ResponseEntity<SignupResDTO> signup(@RequestBody SignupReqDTO reqDTO) {
        return ResponseEntity.ok(memberService.signup(reqDTO));
    }
}
