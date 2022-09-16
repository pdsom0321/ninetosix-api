package com.gsc.ninetosixapi.ninetosix.companyLocation.comtroller;

import com.gsc.ninetosixapi.ninetosix.companyLocation.dto.CompanyLocationsResDTO;
import com.gsc.ninetosixapi.ninetosix.companyLocation.service.CompanyLocationService;
import com.gsc.ninetosixapi.ninetosix.user.dto.UserResDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CompanyLocationController {

    private final CompanyLocationService companyLocationService;

    @GetMapping("/company-locations")
    public ResponseEntity<List<CompanyLocationsResDTO>> companyLocations(@RequestParam String email) {
        return ResponseEntity.ok(companyLocationService.companyLocations(email));
    }
}
