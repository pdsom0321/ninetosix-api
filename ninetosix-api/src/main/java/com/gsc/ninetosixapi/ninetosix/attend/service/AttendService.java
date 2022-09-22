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
import java.time.YearMonth;
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

        attendRepository.findByUserAndAttendDate(user, ymd)
            .map(_attend -> {
                _attend.editOutTime(outTime);
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
        Optional<Attend> attend = attendRepository.findByUserAndAttendDate(user, day);
        if(attend.isPresent()) {
            attend.get().editCode(day, user, code);
        } else {
            attendRepository.save(Attend.createAttendByCode(day, user, code));
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    public List<Attend> attends(@NotNull String email){
        User user = authService.isUser(email);
        String startDate = LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String endDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        List<Attend> attendList = attendRepository.findByUserAndAttendDateBetween(user, startDate, endDate);

        for(int i = attendList.size(); i < 2; i++){
            attendList.add(new Attend());
        }

        return attendList;
    }

    public List<Attend> monthAttends(@NotNull String email, @NotNull String date){
        User user = authService.isUser(email);
        return attendRepository.findByUserAndAttendDateContains(user, date);
    }

}
