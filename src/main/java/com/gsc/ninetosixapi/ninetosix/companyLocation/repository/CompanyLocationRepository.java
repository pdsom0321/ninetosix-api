package com.gsc.ninetosixapi.ninetosix.companyLocation.repository;

import com.gsc.ninetosixapi.ninetosix.company.entity.Company;
import com.gsc.ninetosixapi.ninetosix.companyLocation.entity.CompanyLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyLocationRepository extends JpaRepository<CompanyLocation, Long> {
    List<CompanyLocation> findByCompany(Company company);
}
