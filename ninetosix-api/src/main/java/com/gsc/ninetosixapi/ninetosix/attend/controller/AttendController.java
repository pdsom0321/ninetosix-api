package com.gsc.ninetosixapi.ninetosix.attend.controller;

import com.gsc.ninetosixapi.ninetosix.attend.dto.AttendDto;
import com.gsc.ninetosixapi.ninetosix.attend.service.AttendService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AttendController {
    private final AttendService attendService;

    @PostMapping("/attendance")
    public void attendanceCheck(@RequestBody AttendDto attendDto, Model model){
        attendService.attendanceCheck(attendDto);
    }
}
