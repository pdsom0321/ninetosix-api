package com.gsc.ninetosixapi.ninetosix.company.controller;

import com.gsc.ninetosixapi.ninetosix.company.dto.CompaniesResDTO;
import com.gsc.ninetosixapi.ninetosix.company.service.CompanyService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyController {
    private final CompanyService companyService;

    @ApiOperation(value = "회사 목록")
    @GetMapping("companies")
    public ResponseEntity<List<CompaniesResDTO>> companies() {
        return ResponseEntity.ok(companyService.companies());
    }
}
