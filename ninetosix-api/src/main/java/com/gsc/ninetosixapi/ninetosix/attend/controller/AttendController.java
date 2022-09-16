package com.gsc.ninetosixapi.ninetosix.attend.controller;

import com.gsc.ninetosixapi.ninetosix.attend.dto.AttendCodeReqDTO;
import com.gsc.ninetosixapi.ninetosix.attend.dto.AttendReqDTO;
import com.gsc.ninetosixapi.ninetosix.attend.service.AttendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AttendController {
    private final AttendService attendService;

    @PostMapping("/attend/list")
    public void attendList(@RequestBody AttendReqDTO attendReqDTO){

    }

    @PostMapping("/attend/check")
    public ResponseEntity attendCheck(@RequestBody AttendReqDTO attendReqDTO){

        return ResponseEntity.ok(attendService.attendCheck(attendReqDTO));
    }

    @PostMapping("/attend/code")
    public ResponseEntity attendCode(@RequestBody AttendCodeReqDTO reqDTO) {
        return ResponseEntity.ok(attendService.attendCode(reqDTO));
    }
}
