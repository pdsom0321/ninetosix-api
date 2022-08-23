package com.gsc.ninetosixapi.ninetosix.attend.service;

import com.gsc.ninetosixapi.ninetosix.attend.dto.AttendDto;
import com.gsc.ninetosixapi.ninetosix.attend.entity.Attend;
import com.gsc.ninetosixapi.ninetosix.attend.repository.AttendRepository;
import com.gsc.ninetosixapi.ninetosix.companyLocation.entity.CompanyLocation;
import com.gsc.ninetosixapi.ninetosix.companyLocation.repository.CompanyLocationRepository;
import com.gsc.ninetosixapi.ninetosix.location.entity.Location;
import com.gsc.ninetosixapi.ninetosix.location.repository.LocationRepository;
import com.gsc.ninetosixapi.ninetosix.user.entity.User;
import com.gsc.ninetosixapi.ninetosix.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendService {
    private final AttendRepository attendRepository;
    private final UserRepository userRepository;
    private final CompanyLocationRepository companyLocationRepository;

    public void attendanceCheck(AttendDto attendDto){
        String email = attendDto.getEmail();

        // 유저 출근정보 확인
        Optional<Attend> attendOptional = attendRepository.findByUser(attendDto.getUser());

        // 유저정보 확인
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional != null){
            Optional<CompanyLocation> companyLocationOptional = companyLocationRepository.findById(userOptional.get().getCompanyLocationId());

            // 설정된 위치정보 확인
            if (companyLocationOptional != null) {
                Location location = companyLocationOptional.get().getLocation();

                // 설정된 위치 좌표
                float locationX = location.getX();
                float locationY = location.getY();
                // 유저 현재 위치 좌표
                float userX = attendDto.getX();
                float userY = attendDto.getY();


            }
        }
    }
}
