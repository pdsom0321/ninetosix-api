package com.gsc.ninetosixapi.attend.controller;

import com.gsc.ninetosixapi.attend.service.AttendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AttendController {
    private final AttendService attendService;
}
