package com.gsc.ninetosixapi.ninetosix.attend.controller;

import com.gsc.ninetosixapi.ninetosix.attend.dto.AttendCodeReqDTO;
import com.gsc.ninetosixapi.ninetosix.attend.dto.AttendReqDTO;
import com.gsc.ninetosixapi.ninetosix.attend.dto.AttendResDTO;
import com.gsc.ninetosixapi.ninetosix.attend.service.AttendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AttendController {
    private final AttendService attendService;

    @GetMapping("/attends")
    public ResponseEntity attendList(@RequestParam String email, @RequestParam String startDate, @RequestParam String endDate){
        AttendResDTO attendResDTO = attendService.attendList(email, startDate, endDate);
        return ResponseEntity.ok(attendResDTO);
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
