package com.gsc.ninetosixapi.ninetosix.attend.service;

import com.gsc.ninetosixapi.ninetosix.attend.dto.AttendReqDTO;
import com.gsc.ninetosixapi.ninetosix.attend.entity.Attend;
import com.gsc.ninetosixapi.ninetosix.attend.repository.AttendRepository;
import com.gsc.ninetosixapi.ninetosix.companyLocation.entity.CompanyLocation;
import com.gsc.ninetosixapi.ninetosix.companyLocation.repository.CompanyLocationRepository;
import com.gsc.ninetosixapi.ninetosix.user.entity.User;
import com.gsc.ninetosixapi.ninetosix.user.repository.UserRepository;
import com.gsc.ninetosixapi.ninetosix.vo.AttendCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AttendService {
    private final AttendRepository attendRepository;
    private final UserRepository userRepository;
    private final CompanyLocationRepository companyLocationRepository;

    @Transactional
    public void attendCheck(AttendReqDTO attendReqDTO){
        LocalDateTime currentDateTime = LocalDateTime.now();
        String ymd = currentDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String hms = currentDateTime.format(DateTimeFormatter.ofPattern("HHmmss"));

        User user = userRepository.findByEmail(attendReqDTO.getEmail())
                .orElseThrow(RuntimeException :: new);

        Attend attend = Optional.ofNullable(attendRepository.findByUserAndAttendDate(user, ymd))
                .map(_attend -> {
                    String status = _attend.getStatus();

                    if(AttendCode.ATTEND_CODE_DAY_HOLLY.getAttendCode().equals(status) || AttendCode.ATTEND_CODE_WORK_HOME.equals(status)){
                        // 휴가 또는 재택근무

                    } else if(AttendCode.ATTEND_CODE_WORK_PM.equals(status)) {
                        // PM
                        String yesterday = currentDateTime.minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                        attendRepository.findByUserAndAttendDate(user, yesterday);

                    }

                    return _attend;
                })
                .orElseGet(()->{
                    String status = attendReqDTO.getAttendStatus();
                    CompanyLocation companyLocation = companyLocationRepository.findById(attendReqDTO.getCompanyLocationId())
                            .orElse(null);
                    return Attend.createAttend(ymd, hms, companyLocation, user, status);
                });
        attendRepository.save(attend);
    }
}
