package com.gsc.ninetosixapi.ninetosix.location.service;

import com.gsc.ninetosixapi.ninetosix.companyLocation.entity.CompanyLocation;
import com.gsc.ninetosixapi.ninetosix.location.entity.Location;
import com.gsc.ninetosixapi.ninetosix.location.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LocationService {

    private final LocationRepository locationRepository;

    public List<Location> getLocationByCompanyLocation(CompanyLocation companyLocation) {
        return locationRepository.findByCompanyLocation(companyLocation);
    }
}
