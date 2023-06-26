package com.gsc.ninetosixapi.ninetosix.companyLocation.service;

import com.gsc.ninetosixapi.ninetosix.companyLocation.dto.CompanyLocationsResDTO;
import com.gsc.ninetosixapi.ninetosix.companyLocation.repository.CompanyLocationRepository;
import com.gsc.ninetosixapi.ninetosix.member.entity.Member;
import com.gsc.ninetosixapi.ninetosix.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CompanyLocationService {
    private final CompanyLocationRepository companyLocationRepository;

    private final MemberService memberService;

    public List<CompanyLocationsResDTO> companyLocations(String email) {
        Member member = memberService.findByEmail(email);

        return companyLocationRepository.findByCompany(member.getCompany()).stream()
                .map(CompanyLocationsResDTO::createCompanyLocation)
                .collect(Collectors.toList());
    }
}
