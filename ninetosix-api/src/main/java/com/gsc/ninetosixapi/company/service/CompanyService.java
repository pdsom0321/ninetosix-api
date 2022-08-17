package com.gsc.ninetosixapi.company.service;

import com.gsc.ninetosixapi.company.entity.Company;
import com.gsc.ninetosixapi.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyService {

    private final CompanyRepository companyRepository;

    public Company getCompany(String code) {
        return companyRepository.findByCode(code);
    }
}
