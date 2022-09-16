package com.gsc.ninetosixapi.ninetosix.companyLocation.service;

import com.gsc.ninetosixapi.ninetosix.company.service.CompanyService;
import com.gsc.ninetosixapi.ninetosix.companyLocation.dto.CompanyLocationsResDTO;
import com.gsc.ninetosixapi.ninetosix.companyLocation.entity.CompanyLocation;
import com.gsc.ninetosixapi.ninetosix.companyLocation.repository.CompanyLocationRepository;
import com.gsc.ninetosixapi.ninetosix.location.entity.Location;
import com.gsc.ninetosixapi.ninetosix.location.service.LocationService;
import com.gsc.ninetosixapi.ninetosix.user.entity.User;
import com.gsc.ninetosixapi.ninetosix.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyLocationService {
    private final CompanyLocationRepository companyLocationRepository;

    private final UserService userService;
    private final CompanyService companyService;

    private final LocationService locationService;

    public Optional<CompanyLocation> isCompanyLocation(Long id){
        return companyLocationRepository.findById(id);
    }

    public List<CompanyLocationsResDTO> companyLocations(String email) {
        User user = userService.getUser(email);
        // Company company = companyService.getCompanyByUser(user);
        List<CompanyLocation> companyLocations = companyLocationRepository.findByCompany(user.getCompany());

        for(CompanyLocation companyLocation : companyLocations) {
            List<Location> locations = locationService.getLocationByCompanyLocation(companyLocation);
            System.out.println(locations.get(0).getName());
        }


        return null;
    }
}
