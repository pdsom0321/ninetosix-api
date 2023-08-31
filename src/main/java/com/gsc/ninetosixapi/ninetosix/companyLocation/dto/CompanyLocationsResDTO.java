package com.gsc.ninetosixapi.ninetosix.companyLocation.dto;

import com.gsc.ninetosixapi.ninetosix.companyLocation.entity.CompanyLocation;
import lombok.Builder;

@Builder
public record CompanyLocationsResDTO(
        Long locationId,
        String locationName,
        Float latitude,
        Float longitude) {
    public static CompanyLocationsResDTO createCompanyLocation(CompanyLocation companyLocation) {
        return CompanyLocationsResDTO.builder()
                .locationId(companyLocation.getLocation().getId())
                .locationName(companyLocation.getLocation().getName())
                .latitude(companyLocation.getLocation().getLatitude())
                .longitude(companyLocation.getLocation().getLongitude())
                .build();
    }
}
