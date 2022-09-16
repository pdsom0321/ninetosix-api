package com.gsc.ninetosixapi.ninetosix.companyLocation.dto;

import com.gsc.ninetosixapi.ninetosix.companyLocation.entity.CompanyLocation;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CompanyLocationsResDTO {
    String companyLocationCode;
    String companyCode;
    String locationCode;
    String locationName;
    Float latitude;
    Float longitude;

    public static CompanyLocationsResDTO createCompanyLocation(CompanyLocation companyLocation) {
        return CompanyLocationsResDTO.builder()
                .companyLocationCode(companyLocation.getCode())
                .companyCode(companyLocation.getCompany().getCode())
                .locationCode(companyLocation.getLocation().getCode())
                .locationName(companyLocation.getLocation().getName())
                .latitude(companyLocation.getLocation().getLatitude())
                .longitude(companyLocation.getLocation().getLongitude())
                .build();
    }
}
