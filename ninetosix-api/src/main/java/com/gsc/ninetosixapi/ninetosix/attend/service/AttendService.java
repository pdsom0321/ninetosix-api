package com.gsc.ninetosixapi.ninetosix.attend.service;

import com.gsc.ninetosixapi.ninetosix.attend.dto.AttendCodeReqDTO;
import com.gsc.ninetosixapi.ninetosix.attend.dto.AttendReqDTO;
import com.gsc.ninetosixapi.ninetosix.attend.dto.AttendResDTO;
import com.gsc.ninetosixapi.ninetosix.attend.entity.Attend;
import com.gsc.ninetosixapi.ninetosix.attend.repository.AttendRepository;
import com.gsc.ninetosixapi.ninetosix.attend.repository.AttendSpecification;
import com.gsc.ninetosixapi.ninetosix.user.entity.User;
import com.gsc.ninetosixapi.ninetosix.user.service.AuthService;
import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AttendService {
    private final AttendRepository attendRepository;
    private final AuthService authService;

    public ResponseEntity attendCheck(@NotNull AttendReqDTO reqDTO){
        String ymd = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String inTime = reqDTO.getInTime();
        String outTime = reqDTO.getOutTime();
        String attendCode = reqDTO.getAttendCode();
        String locationCode = reqDTO.getLocationCode();
        User user = authService.isUser(reqDTO.getEmail());

        Attend attend = attendRepository.findByUserAndAttendDate(user, ymd)
                .map(_attend -> {
                    _attend.changeOutTime(outTime);
                    return _attend;
                })
                .orElseGet(() -> {
                    String _attendCode = Optional.ofNullable(attendCode)
                            .filter(code -> !code.isEmpty() && !code.isBlank())
                            .orElse("ST01");
                    return attendRepository.save(Attend.createAttend(ymd, inTime, locationCode, user, _attendCode));
                });
        return new ResponseEntity(HttpStatus.OK);
    }

    public ResponseEntity attendCode(@NotNull AttendCodeReqDTO reqDTO) {
        String day = reqDTO.getDate();
        String code = reqDTO.getAttendCode();
        String email = reqDTO.getEmail();

        User user = authService.isUser(email);

        Attend attend = attendRepository.findByUserAndAttendDate(user, day)
                .map(_attend -> {
                    _attend.editCode(day, user, code);
                    return _attend;
                })
                .orElseGet(() -> attendRepository.save(Attend.addCode(day, user, code)));

        return new ResponseEntity(HttpStatus.OK);
    }

    public AttendResDTO attendList(@NotNull String email, String startDate, String endDate){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyyMM");

        String startDay = "01";
        //String endDay =
        String startTime = "000000";
        String endTime = "235959";


        String _yesterday = LocalDateTime.now().minusDays(1).format(dateFormatter) + startTime;
        String _today = LocalDateTime.now().format(dateFormatter) + endTime;

        if(!startDate.isEmpty() && !endDate.isEmpty()){
            if(!startDate.isBlank() && !endDate.isBlank()){

            }
        }


        LocalDateTime today = LocalDateTime.parse(_today, dateTimeFormatter);
        LocalDateTime yesterday = LocalDateTime.parse(_yesterday, dateTimeFormatter);
        return new AttendResDTO(attendRepository.findAll(AttendSpecification.betweenYesterdayAndToday(yesterday, today)));
    }
}
