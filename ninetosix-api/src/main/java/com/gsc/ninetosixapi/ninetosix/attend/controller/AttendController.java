package com.gsc.ninetosixapi.ninetosix.attend.controller;

import com.gsc.ninetosixapi.ninetosix.attend.dto.AttendCodeReqDTO;
import com.gsc.ninetosixapi.ninetosix.attend.dto.AttendReqDTO;
import com.gsc.ninetosixapi.ninetosix.attend.dto.AttendResDTO;
import com.gsc.ninetosixapi.ninetosix.attend.service.AttendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AttendController {
    private final AttendService attendService;

    @GetMapping("/attends")
    public ResponseEntity attends(Authentication authentication){
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        String email = userDetails.getUsername();
        return ResponseEntity.ok(new AttendResDTO(attendService.attends(email)));
    }

    @GetMapping("/attends/{month}")
    public ResponseEntity attendsMonth(Authentication authentication, @PathVariable String month){
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        String email = userDetails.getUsername();
        return ResponseEntity.ok(new AttendResDTO(attendService.attendsMonth(email, month)));
    }

    @PostMapping("/attend")
    public ResponseEntity attendCheck(@RequestBody AttendReqDTO attendReqDTO){
        return ResponseEntity.ok(attendService.attendCheck(attendReqDTO));
    }

    @PostMapping("/attend/code")
    public ResponseEntity attendCode(@RequestBody AttendCodeReqDTO reqDTO) {
        return ResponseEntity.ok(attendService.attendCode(reqDTO));
    }
}
