package com.gsc.ninetosixapi.ninetosix.attend.controller;

import com.gsc.ninetosixapi.ninetosix.attend.dto.AttendReqDTO;
import com.gsc.ninetosixapi.ninetosix.attend.service.AttendService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AttendController {
    private final AttendService attendService;

    @PostMapping("/attend/list")
    public void attendList(@RequestBody AttendReqDTO attendReqDTO, Model model){

    }

    @PostMapping("/attend/check")
    public void attendCheck(@RequestBody AttendReqDTO attendReqDTO, Model model){
        attendService.attendCheck(attendReqDTO);
    }
}
