package com.gsc.ninetosixapi.ninetosix.user.controller;

import com.gsc.ninetosixapi.ninetosix.user.dto.CodeCheckReqDTO;
import com.gsc.ninetosixapi.ninetosix.user.dto.CodeSendReqDTO;
import com.gsc.ninetosixapi.ninetosix.user.service.AuthCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth/code")
public class AuthCodeController {

    private final AuthCodeService authCodeService;

    @GetMapping("/send")
    public void sendCode(@ModelAttribute CodeSendReqDTO reqDTO) {
        authCodeService.sendCode(reqDTO);
    }

    @GetMapping("/check")
    public Boolean checkCode(@ModelAttribute CodeCheckReqDTO reqDTO) {
        return authCodeService.checkCode(reqDTO);
    }

}
