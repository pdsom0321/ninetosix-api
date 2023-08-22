package com.gsc.ninetosixapi.ninetosix.log.controller;

import com.gsc.ninetosixapi.ninetosix.log.dto.ErrorLogReqDTO;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LogController {

    @ApiOperation(value = "front(vue)에서 error 발생 시 파악하기 위한 로그. front error 전용 로그")
    @PostMapping("error-log")
    public void errorLogs(@RequestBody ErrorLogReqDTO reqDTO) {
        log.info(reqDTO.msg());
    }
}
