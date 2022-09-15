package com.gsc.ninetosixapi.ninetosix.companyLocation.service;

import com.gsc.ninetosixapi.ninetosix.companyLocation.entity.CompanyLocation;
import com.gsc.ninetosixapi.ninetosix.companyLocation.repository.CompanyLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyLocationService {
    private final CompanyLocationRepository companyLocationRepository;

    public Optional<CompanyLocation> isCompanyLocation(Long id){
        return companyLocationRepository.findById(id);
    }
}
