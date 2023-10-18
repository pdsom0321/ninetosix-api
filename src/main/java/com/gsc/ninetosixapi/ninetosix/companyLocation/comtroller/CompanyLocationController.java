package com.gsc.ninetosixapi.ninetosix.companyLocation.comtroller;

import com.gsc.ninetosixapi.core.aspect.UserId;
import com.gsc.ninetosixapi.ninetosix.companyLocation.dto.CompanyLocationsResDTO;
import com.gsc.ninetosixapi.ninetosix.companyLocation.service.CompanyLocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CompanyLocationController {

    private final CompanyLocationService companyLocationService;

    @UserId
    @GetMapping("/company-locations")
    public ResponseEntity<List<CompanyLocationsResDTO>> companyLocations(HttpServletRequest request) {
        long memberId = (Long) request.getAttribute("memberId");
        return ResponseEntity.ok(companyLocationService.companyLocations(memberId));
    }
}
