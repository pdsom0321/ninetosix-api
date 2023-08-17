package com.gsc.ninetosixapi.ninetosix.company.dto;

import com.gsc.ninetosixapi.ninetosix.company.entity.Company;

public record CompaniesResDTO(Long id, String name) {
    public static CompaniesResDTO of(Company company) {
        return new CompaniesResDTO(company.getId(), company.getName());
    }
}
