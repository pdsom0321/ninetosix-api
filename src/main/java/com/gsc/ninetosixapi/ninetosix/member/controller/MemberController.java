package com.gsc.ninetosixapi.ninetosix.member.controller;

import com.gsc.ninetosixapi.ninetosix.member.dto.SignupReqDTO;
import com.gsc.ninetosixapi.ninetosix.member.dto.SignupResDTO;
import com.gsc.ninetosixapi.ninetosix.member.service.MemberService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("member")
public class MemberController {
    private MemberService memberService;

    @ApiOperation(value = "회원가입", notes = "")
    @PostMapping("new")
    public ResponseEntity<SignupResDTO> signup(@RequestBody SignupReqDTO reqDTO) {
        return ResponseEntity.ok(memberService.signup(reqDTO));
    }
}
