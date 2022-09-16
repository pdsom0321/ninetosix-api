package com.gsc.ninetosixapi.ninetosix.company.service;

import com.gsc.ninetosixapi.ninetosix.company.entity.Company;
import com.gsc.ninetosixapi.ninetosix.company.repository.CompanyRepository;
import com.gsc.ninetosixapi.ninetosix.user.entity.User;
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
