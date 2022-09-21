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
    public ResponseEntity attends(){
        return ResponseEntity.ok(new AttendResDTO(attendService.attends("it1485@gsitm.com")));
    }

    @GetMapping("/attends/month")
    public ResponseEntity monthAttends(@RequestParam String date){
        return ResponseEntity.ok(new AttendResDTO(attendService.monthAttends("it1485@gsitm.com", date)));
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
