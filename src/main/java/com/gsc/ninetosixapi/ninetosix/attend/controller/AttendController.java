package com.gsc.ninetosixapi.ninetosix.attend.controller;

import com.gsc.ninetosixapi.ninetosix.attend.dto.AttendCodeReqDTO;
import com.gsc.ninetosixapi.ninetosix.attend.dto.AttendReqDTO;
import com.gsc.ninetosixapi.ninetosix.attend.dto.AttendResDTO;
import com.gsc.ninetosixapi.ninetosix.attend.service.AttendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AttendController {
    private final AttendService attendService;

    @GetMapping("/attends")
    public ResponseEntity attends(@ApiIgnore Principal principal){
        return ResponseEntity.ok(new AttendResDTO(attendService.getAttendanceList(principal.getName())));
    }

    @GetMapping("/attends/{month}")
    public ResponseEntity attendsMonth(@ApiIgnore Principal principal, @PathVariable String month){
        String email = principal.getName();
        return ResponseEntity.ok(new AttendResDTO(attendService.attendsMonth(email, month)));
    }

    @PostMapping("/attend")
    public ResponseEntity attendCheck(@ApiIgnore Principal principal, @RequestBody AttendReqDTO attendReqDTO){
        attendService.processAttendance(principal.getName(), attendReqDTO);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/attend/code")
    public ResponseEntity createAttendByCode(@ApiIgnore Principal principal, @RequestBody AttendCodeReqDTO reqDTO) {
        return ResponseEntity.ok(attendService.processAttendanceByCode(principal.getName(), reqDTO));
    }
}
