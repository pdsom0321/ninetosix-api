package com.gsc.ninetosixapi.ninetosix.attend.service;

import com.gsc.ninetosixapi.ninetosix.attend.dto.AttendCodeReqDTO;
import com.gsc.ninetosixapi.ninetosix.attend.dto.AttendReqDTO;
import com.gsc.ninetosixapi.ninetosix.attend.entity.Attend;
import com.gsc.ninetosixapi.ninetosix.attend.repository.AttendRepository;
import com.gsc.ninetosixapi.ninetosix.companyLocation.entity.CompanyLocation;
import com.gsc.ninetosixapi.ninetosix.companyLocation.repository.CompanyLocationRepository;
import com.gsc.ninetosixapi.ninetosix.companyLocation.service.CompanyLocationService;
import com.gsc.ninetosixapi.ninetosix.user.entity.User;
import com.gsc.ninetosixapi.ninetosix.user.repository.UserRepository;
import com.gsc.ninetosixapi.ninetosix.user.service.AuthService;
import com.gsc.ninetosixapi.ninetosix.vo.AttendCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AttendService {
    private final AttendRepository attendRepository;
    private final AuthService authService;
    private final CompanyLocationService companyLocationService;

    @Transactional
    public void attendCheck(AttendReqDTO attendReqDTO){
        LocalDateTime currentDateTime = LocalDateTime.now();
        String ymd = currentDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String hms = currentDateTime.format(DateTimeFormatter.ofPattern("HHmmss"));

        User user = authService.isUser(attendReqDTO.getEmail());

        Attend attend = Optional.ofNullable(attendRepository.findByUserAndAttendDate(user, ymd))
                .map(_attend -> {
                    String status = _attend.getAttendCode();

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
                    CompanyLocation companyLocation = companyLocationService.isCompanyLocation(attendReqDTO.getCompanyLocationId())
                            .orElse(null);
                    return Attend.createAttend(ymd, hms, companyLocation, user, status);
                });
        attendRepository.save(attend);
    }

    public ResponseEntity attendCode(AttendCodeReqDTO reqDTO) {
        String day = reqDTO.getDate();
        String code = reqDTO.getAttendCode();
        String email = reqDTO.getEmail();

        User user = authService.isUser(email);
        Attend attend = attendRepository.findByUserAndAttendDate(user, day);
        if(attend != null) {
            attend.editCode(day, user, code);
        } else {
            attendRepository.save(Attend.addCode(day, user, code));
        }

        return new ResponseEntity(HttpStatus.OK);
    }
}
