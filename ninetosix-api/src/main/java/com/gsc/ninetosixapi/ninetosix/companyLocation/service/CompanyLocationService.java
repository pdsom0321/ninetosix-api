package com.gsc.ninetosixapi.ninetosix.companyLocation.service;

import com.gsc.ninetosixapi.ninetosix.companyLocation.dto.CompanyLocationsResDTO;
import com.gsc.ninetosixapi.ninetosix.companyLocation.entity.CompanyLocation;
import com.gsc.ninetosixapi.ninetosix.companyLocation.repository.CompanyLocationRepository;
import com.gsc.ninetosixapi.ninetosix.user.entity.User;
import com.gsc.ninetosixapi.ninetosix.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyLocationService {
    private final CompanyLocationRepository companyLocationRepository;

    private final UserService userService;

    public CompanyLocation isCompanyLocation(Long id){
        return companyLocationRepository.findById(id)
                .orElseThrow(() -> {return new RuntimeException("위치 매핑 정보가 없습니다.");});
    }

    public List<CompanyLocationsResDTO> companyLocations(String email) {
        User user = userService.getUser(email);

        return companyLocationRepository.findByCompany(user.getCompany()).stream()
                .map(CompanyLocationsResDTO::createCompanyLocation)
                .collect(Collectors.toList());
    }
}
