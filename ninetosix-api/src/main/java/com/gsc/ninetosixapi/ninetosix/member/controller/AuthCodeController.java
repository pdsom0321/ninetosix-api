package com.gsc.ninetosixapi.ninetosix.member.controller;

import com.gsc.ninetosixapi.ninetosix.member.dto.CodeCheckReqDTO;
import com.gsc.ninetosixapi.ninetosix.member.dto.CodeSendReqDTO;
import com.gsc.ninetosixapi.ninetosix.member.service.AuthCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthCodeController {

    private final AuthCodeService authCodeService;

    @GetMapping("/auth/code")
    public void sendCode(@ModelAttribute CodeSendReqDTO reqDTO) {
        authCodeService.sendCode(reqDTO);
    }

    @PutMapping("/auth/code")
    public Boolean checkCode(@ModelAttribute CodeCheckReqDTO reqDTO) {
        return authCodeService.checkCode(reqDTO);
    }

}
