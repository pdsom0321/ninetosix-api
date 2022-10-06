package com.gsc.ninetosixapi.ninetosix.attend.controller;

import com.gsc.ninetosixapi.ninetosix.attend.dto.AttendCodeReqDTO;
import com.gsc.ninetosixapi.ninetosix.attend.dto.AttendReqDTO;
import com.gsc.ninetosixapi.ninetosix.attend.dto.AttendResDTO;
import com.gsc.ninetosixapi.ninetosix.attend.service.AttendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AttendController {
    private final AttendService attendService;

    @GetMapping("/attends")
    public ResponseEntity attends(Principal principal){
        //String email = principal.getName();
        String email = "it1485@gsitm.com";
        return ResponseEntity.ok(new AttendResDTO(attendService.attends(email)));
    }

    @GetMapping("/attends/{month}")
    public ResponseEntity attendsMonth(Principal principal, @PathVariable String month){
        String email = principal.getName();
        return ResponseEntity.ok(new AttendResDTO(attendService.attendsMonth(email, month)));
    }

    @PostMapping("/attend")
    public ResponseEntity attendCheck(@RequestBody AttendReqDTO attendReqDTO){
        return ResponseEntity.ok(attendService.attendCheck(attendReqDTO));
    }

    @PostMapping("/attend/code")
    public ResponseEntity createAttendByCode(@RequestBody AttendCodeReqDTO reqDTO) {
        return ResponseEntity.ok(attendService.createAttendByCode(reqDTO));
    }
}
