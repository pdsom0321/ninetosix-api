package com.gsc.ninetosixapi.ninetosix.code.controller;

import com.gsc.ninetosixapi.ninetosix.code.dto.CodesReqDTO;
import com.gsc.ninetosixapi.ninetosix.code.dto.CodesResDTO;
import com.gsc.ninetosixapi.ninetosix.code.service.CodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CodeController {

    private final CodeService codeService;

    @GetMapping("/codes")
    public ResponseEntity<List<CodesResDTO>> getCodes(CodesReqDTO reqDTO) {
        return ResponseEntity.ok(codeService.getCodes(reqDTO.getCodeGroup()));
    }
}
