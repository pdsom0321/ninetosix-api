package com.gsc.ninetosixapi.ninetosix.companyLocation.repository;

import com.gsc.ninetosixapi.ninetosix.companyLocation.entity.CompanyLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyLocationRepository extends JpaRepository<CompanyLocation, Long> {
    Optional<CompanyLocation> findById(Long id);
}
