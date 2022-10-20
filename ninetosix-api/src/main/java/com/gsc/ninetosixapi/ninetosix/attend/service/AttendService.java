package com.gsc.ninetosixapi.ninetosix.attend.service;

import com.gsc.ninetosixapi.ninetosix.attend.dto.AttendCodeReqDTO;
import com.gsc.ninetosixapi.ninetosix.attend.dto.AttendReqDTO;
import com.gsc.ninetosixapi.ninetosix.attend.entity.Attend;
import com.gsc.ninetosixapi.ninetosix.attend.repository.AttendRepository;
import com.gsc.ninetosixapi.ninetosix.member.entity.Member;
import com.gsc.ninetosixapi.ninetosix.member.service.AuthService;
import com.gsc.ninetosixapi.ninetosix.vo.AttendCode;
import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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

    public ResponseEntity attendCheck(String email, @NotNull AttendReqDTO reqDTO){
        String yesterday = LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String ymd = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String inTime = reqDTO.getInTime();
        String outTime = reqDTO.getOutTime();
        String attendCode = reqDTO.getAttendCode();
        String locationCode = reqDTO.getLocationCode();
        Member member = authService.getMember(email);

        Optional<Attend> attend = attendRepository.findByMemberAndAttendDate(member, ymd);

        if(!attend.isPresent()){
            Attend _attend = attendRepository.save(Attend.createAttend(ymd, locationCode, member, attendCode));

            if(inTime != null && !inTime.isEmpty() && !inTime.isBlank()) {
                _attend.updateInTime(inTime);
            } else if(outTime != null && !outTime.isEmpty() && !outTime.isBlank()) {
                _attend.updateOutTime(outTime);
            }

        } else {
            attend.get().updateOutTime(outTime);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    public ResponseEntity createAttendByCode(@NotNull AttendCodeReqDTO reqDTO) {
        String day = reqDTO.getDate();
        String code = reqDTO.getAttendCode();
        String email = reqDTO.getEmail();

        Member member = authService.getMember(email);
        Optional<Attend> attend = attendRepository.findByMemberAndAttendDate(member, day);
        if(attend.isPresent()) {
            attend.get().updateCode(member, code);
        } else {
            attendRepository.save(Attend.createAttendByCode(day, member, code));
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    public List<Attend> attends(@NotNull String email){
        Member member = authService.getMember(email);
        String startDate = LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String endDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        List<Attend> attendList = attendRepository.findTop2ByMemberAndAttendDateBetweenOrderByAttendDateAsc(member, startDate, endDate);

        if(attendList != null) {
            if (attendList.size() == 1) {
                if (attendList.get(0).getAttendDate().equals(endDate)) {
                    attendList.add(0, new Attend());
                }
            }
        }

        for(int i = attendList.size(); i < 2; i++){
            attendList.add(new Attend());
        }

        return attendList;
    }

    public List<Attend> attendsMonth(@NotNull String email, @NotNull String month){
        Member member = authService.getMember(email);
        return attendRepository.findByMemberAndAttendDateContains(member, month);
    }

}
