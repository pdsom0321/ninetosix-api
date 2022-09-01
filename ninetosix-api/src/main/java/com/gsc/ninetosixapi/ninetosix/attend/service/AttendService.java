package com.gsc.ninetosixapi.ninetosix.attend.service;

import com.gsc.ninetosixapi.ninetosix.attend.dto.AttendDto;
import com.gsc.ninetosixapi.ninetosix.attend.entity.Attend;
import com.gsc.ninetosixapi.ninetosix.attend.repository.AttendRepository;
import com.gsc.ninetosixapi.ninetosix.companyLocation.entity.CompanyLocation;
import com.gsc.ninetosixapi.ninetosix.companyLocation.repository.CompanyLocationRepository;
import com.gsc.ninetosixapi.ninetosix.user.entity.User;
import com.gsc.ninetosixapi.ninetosix.user.repository.UserRepository;
import com.gsc.ninetosixapi.ninetosix.vo.AttendStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.gsc.ninetosixapi.ninetosix.vo.AttendStatus.*;

@Service
@RequiredArgsConstructor
public class AttendService {
    private final AttendRepository attendRepository;
    private final UserRepository userRepository;
    private final CompanyLocationRepository companyLocationRepository;

    @Transactional
    public void attendanceCheck(AttendDto attendDto){
        String status = attendDto.getUserStatus();
        LocalDateTime currentDateTime = LocalDateTime.now();
        String ymd = currentDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String hms = currentDateTime.format(DateTimeFormatter.ofPattern("HHmmss"));

        // TODO JWT 상태 확인 로직 필요
        switch (status){
            case "ST001":
            {
                // 정상 출근
                User user = userRepository.findByEmail(attendDto.getEmail()).get();
                CompanyLocation companyLocation = companyLocationRepository.findById(attendDto.getCompanyLocationId()).get();

                if(attendRepository.findByUserAndAttendDate(user, ymd).isEmpty()){
                    attendRepository.save(Attend.createAttend(ymd, hms, null, user, companyLocation, status));
                }
                // 정상 퇴근
                else {
                    attendRepository.save(Attend.createAttend(ymd, null, hms, user, companyLocation, status));
                }
            }
            case "ST006":
            {
                // 재택 근무
            }
            case "ST007":
            {
                // 휴가
            }
        }
    }
}
