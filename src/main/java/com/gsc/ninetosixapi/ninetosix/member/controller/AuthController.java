package com.gsc.ninetosixapi.ninetosix.member.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    /*@PostMapping("/reissue")
    public ResponseEntity reissue(@ApiIgnore Principal principal, @RequestBody TokenReqDTO reqDTO) {
        return ResponseEntity.ok(tokenProvider.reissue(reqDTO, memberService.getMemberByEmail(principal.getName())));
    }*/
}
