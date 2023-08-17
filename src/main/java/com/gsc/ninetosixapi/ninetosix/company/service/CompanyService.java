package com.gsc.ninetosixapi.ninetosix.company.service;

import com.gsc.ninetosixapi.ninetosix.company.dto.CompaniesResDTO;
import com.gsc.ninetosixapi.ninetosix.company.entity.Company;
import com.gsc.ninetosixapi.ninetosix.company.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyService {

    private final CompanyRepository companyRepository;

    public List<CompaniesResDTO> companies() {
        return companyRepository.findAll().stream()
                .map(CompaniesResDTO::of)
                .collect(Collectors.toList());
    }

    public Company getCompany(Long id) {
        return companyRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

}
