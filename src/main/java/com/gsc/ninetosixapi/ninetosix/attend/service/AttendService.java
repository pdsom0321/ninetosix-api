package com.gsc.ninetosixapi.ninetosix.attend.service;

import com.gsc.ninetosixapi.ninetosix.attend.util.DateTimeUtil;
import com.gsc.ninetosixapi.ninetosix.attend.dto.AttendCodeReqDTO;
import com.gsc.ninetosixapi.ninetosix.attend.dto.AttendReqDTO;
import com.gsc.ninetosixapi.ninetosix.attend.entity.Attend;
import com.gsc.ninetosixapi.ninetosix.attend.repository.AttendRepository;
import com.gsc.ninetosixapi.ninetosix.member.entity.Member;
import com.gsc.ninetosixapi.ninetosix.member.service.AuthService;
import com.gsc.ninetosixapi.ninetosix.vo.AttendCode;
import com.gsc.ninetosixapi.ninetosix.vo.TimeCode;
import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AttendService {
    private final AttendRepository attendRepository;
    private final AuthService authService;
    private final DateTimeUtil dateTimeUtil;

    public ResponseEntity attendCheck(String email, @NotNull AttendReqDTO reqDTO){
        String ymd = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String inTime = reqDTO.getInTime();
        String outTime = reqDTO.getOutTime();
        String attendCode = reqDTO.getAttendCode();
        String locationCode = reqDTO.getLocationCode();
        Member member = authService.getMember(email);

        Optional<Attend> optionalAttend = attendRepository.findByMemberAndAttendDate(member, ymd);

        // DB Table에 금일 데이터 유/무 체크
        if(!optionalAttend.isPresent()){
            // create attend object and insert
            optionalAttend = Optional.of(attendRepository.save(Attend.createAttend(ymd, locationCode, member, attendCode)));
        } else {
            // attend code update
            optionalAttend.get().updateCode(member, attendCode);
        }

        if(inTime != null && !inTime.isEmpty() && !inTime.isBlank()) {
            optionalAttend.get().updateInTime(inTime);
        } else if(outTime != null && !outTime.isEmpty() && !outTime.isBlank()) {
            optionalAttend.get().updateOutTime(outTime);
        }

        return new ResponseEntity(HttpStatus.OK);
    }

    public ResponseEntity createAttendByCode(@NotNull String email, @NotNull AttendCodeReqDTO reqDTO) {
        String code = reqDTO.getAttendCode();
        int from = reqDTO.getFrom();
        int to = reqDTO.getTo();
        log.info("================from : " + from);
        log.info("================to : " + to);

        int current = from;
        while(current <= to) {
            log.info("================current : " + current);
            Member member = authService.getMember(email);
            Optional<Attend> attend = attendRepository.findByMemberAndAttendDate(member, String.valueOf(current));
            if(attend.isPresent()) {
                attend.get().updateCode(member, code);
            } else {
                attendRepository.save(Attend.createAttendByCode(String.valueOf(current), member, code));
            }
            current ++;
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
        List<Attend> attendList = attendRepository.findByMemberAndAttendDateContainsOrderByAttendDateAsc(member, month);
        calWorkTime(attendList);
        return attendList;
    }

    /**
     * 1. 11시 30분 ~ 12시 30분 점심시간시 근무시간에서 제외
     * 2.
     **/
    public void calWorkTime(List<Attend> attendList){
        String ofPattern = "yyyyMMddHHmmss";

        for (int i = 0; i < attendList.size(); i++){
            Attend attend = attendList.get(i);

            String date = attend.getAttendDate();
            String in = attend.getInTime();
            String out = attend.getOutTime();

            Duration duration = null;
            LocalDateTime inTime = null;
            LocalDateTime outTime = null;

            AttendCode code = AttendCode.findByStatusCode(attend.getAttendCode());

            try{
                if(in != null) {
                    inTime = dateTimeUtil.calWorkTime(date, in, ofPattern);
                }
                if(out != null){
                    outTime = dateTimeUtil.calWorkTime(date, out, ofPattern);
                }

                switch (code){
                    case ATTEND_CODE_DAY_NORMAL :
                    case ATTEND_CODE_DAY_QUARTER_MORNING :
                    case ATTEND_CODE_DAY_QUARTER_AFTERNOON :
                    case ATTEND_CODE_WORK_HOME :
                        if(in == null) {
                            inTime = dateTimeUtil.getWorkTime(date, "TC01", ofPattern);

                            // 전날 PM일 경우
                            if(i != 0 && attendList.get(i-1).getAttendCode().equals(AttendCode.ATTEND_CODE_WORK_PM.getAttendCode())){
                                inTime = dateTimeUtil.getWorkTime(date, "TC13", ofPattern);
                            }
                        }

                        if(out == null) {
                            outTime = dateTimeUtil.getWorkTime(date, "TC02", ofPattern);
                        }
                        duration = Duration.between(inTime, outTime);
                        duration.minusMinutes(60);

                        attend.updateWorkTime(duration.toHours(), duration.toMinutesPart());
                        break;

                    case ATTEND_CODE_DAY_HALF_MORNING :
                        if(in == null) {
                            inTime = dateTimeUtil.getWorkTime(date, "TC03", ofPattern);
                        }

                        if(out == null){
                            outTime = dateTimeUtil.getWorkTime(date, "TC04", ofPattern);
                        }

                        duration = Duration.between(inTime, outTime);
                        duration.minusMinutes(60);
                        attend.updateWorkTime(duration.toHours(), duration.toMinutesPart());
                        break;

                    case ATTEND_CODE_DAY_HALF_AFTERNOON :
                        if(in == null) {
                            inTime = dateTimeUtil.getWorkTime(date, "TC05", ofPattern);
                        }

                        if(out == null){
                            outTime = dateTimeUtil.getWorkTime(date, "TC06", ofPattern);
                        }
                        duration = Duration.between(inTime, outTime);
                        attend.updateWorkTime(duration.toHours(), duration.toMinutesPart());
                        break;

                    case ATTEND_CODE_WORK_PM :
                        if(in == null) {
                            inTime = dateTimeUtil.getWorkTime(date, "TC13", ofPattern);
                        }

                        if (out == null) {
                            outTime =  dateTimeUtil.getWorkTime(date, "TC13", ofPattern).plusDays(1);
                        }
                        duration = Duration.between(inTime, outTime);
                        duration.minusMinutes(60);
                        break;

                    case ATTEND_CODE_DAY_HOLLY:
                        if(in != null && out != null) {
                            LocalDateTime lunch_start = dateTimeUtil.getWorkTime(date, "TC11", ofPattern);
                            LocalDateTime lunch_end = dateTimeUtil.getWorkTime(date, "TC12", ofPattern);

                            if(outTime.isAfter(lunch_start) && outTime.isBefore(lunch_end)){
                                outTime = lunch_start;
                            }
                            duration = Duration.between(inTime, outTime);

                            if(outTime.isAfter(lunch_end)){
                                duration.minusMinutes(60);
                            }
                        } else {
                            attend.updateWorkTime(0, 0);
                        }
                        break;
                }
                attend.updateWorkTime(duration.toHours(), duration.toMinutesPart());
            } catch(Exception e){
                //throw new RuntimeException("");
            }
        }
    }
}