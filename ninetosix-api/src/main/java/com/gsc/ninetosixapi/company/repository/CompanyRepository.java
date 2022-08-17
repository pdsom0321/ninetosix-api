package com.gsc.ninetosixapi.company.repository;

import com.gsc.ninetosixapi.company.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
    Company findByCode(String code);
}
