package com.gsc.ninetosixapi.ninetosix.location.repository;

import com.gsc.ninetosixapi.ninetosix.companyLocation.entity.CompanyLocation;
import com.gsc.ninetosixapi.ninetosix.location.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findById(Long id);

    List<Location> findByCompanyLocation(CompanyLocation companyLocation);
}
