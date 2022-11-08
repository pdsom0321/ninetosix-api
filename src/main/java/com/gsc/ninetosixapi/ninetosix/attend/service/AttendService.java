package com.gsc.ninetosixapi.ninetosix.attend.service;

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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class AttendService {
    private final AttendRepository attendRepository;
    private final AuthService authService;

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
        List<Attend> attendList = attendRepository.findByMemberAndAttendDateContains(member, month);
        calWorkTime(attendList);
        return attendList;
    }

    /**
     * 1. 11시 30분 ~ 12시 30분 점심시간시 근무시간에서 제외
     * 2.
     **/
    public void calWorkTime(List<Attend> attendList){
        DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("yyyyMMddHHmmss", Locale.KOREA);

        for (int i = 0; i < attendList.size(); i++){
            Attend attend = attendList.get(i);

            String date = attend.getAttendDate();
            String in = attend.getInTime();
            String out = attend.getOutTime();

            LocalDateTime curTime = LocalDateTime.now();
            LocalDateTime startLunchTime = LocalDateTime.parse(date + TimeCode.TIME_CODE_START_LUNCH.getTimeCodeValue(), ofPattern);    // 점심 시작 시간
            LocalDateTime endLunchTime = LocalDateTime.parse(date + TimeCode.TIME_CODE_END_LUNCH.getTimeCodeValue(), ofPattern);        // 점심 종료 시간
            LocalDateTime startWorkTimeFm = LocalDateTime.parse(date + TimeCode.TIME_CODE_START_WORK.getTimeCodeValue(), ofPattern);    // 정상 출근 시간
            LocalDateTime endWorkTimeFm = LocalDateTime.parse(date + TimeCode.TIME_CODE_END_WORK.getTimeCodeValue(), ofPattern);        // 정상 퇴근 시간
            LocalDateTime midnight = LocalDateTime.parse(date + TimeCode.TIME_CODE_DAY_MIDNIGHT.getTimeCodeValue(), ofPattern);         // 자정

            Duration duration = null;
            LocalDateTime inTime = null;
            LocalDateTime outTime = null;

            AttendCode code = AttendCode.findByStatusCode(attend.getAttendCode());

            try{
                switch (code){
                    case ATTEND_CODE_DAY_NORMAL :
                    case ATTEND_CODE_DAY_HALF_MORNING :
                    case ATTEND_CODE_DAY_HALF_HALF_MORNING :
                    case ATTEND_CODE_DAY_HALF_HALF_AFTERNOON :
                    case ATTEND_CODE_WORK_HOME :
                        if(in != null && out != null) {
                            inTime = LocalDateTime.parse(date + in, ofPattern);
                            outTime = LocalDateTime.parse(date + out, ofPattern);
                        }
                        else if(in != null && out == null) {
                            inTime = LocalDateTime.parse(date + in, ofPattern);

                            if(curTime.isAfter(startLunchTime) && curTime.isBefore(endLunchTime)){
                                // 점심시간
                                outTime = startLunchTime;
                            } else if((curTime.isAfter(startWorkTimeFm) && curTime.isBefore(startLunchTime) || (curTime.isAfter(endLunchTime) && curTime.isBefore(endWorkTimeFm)))){
                                // 근무시간
                                outTime = LocalDateTime.parse(curTime.format(ofPattern), ofPattern);
                            } else if(curTime.isAfter(endWorkTimeFm)){
                                // 퇴근
                                outTime = endWorkTimeFm;
                            }
                        }
                        else if(in == null && out != null) {
                            // PM일 경우에만 고려
                            inTime = startWorkTimeFm;

                            if(i != 0 && attendList.get(i-1).getAttendCode().equals(AttendCode.ATTEND_CODE_WORK_PM.getAttendCode())){
                                inTime = midnight;
                            }
                        }

                        duration = Duration.between(inTime, outTime);
                        // 점심시간 근무시간에서 제외
                        if(curTime.isAfter(endLunchTime)){
                            duration.minusMinutes(60);
                        }

                        attend.updateWorkTime(duration.toHours(), duration.toMinutesPart());
                        break;
                    case ATTEND_CODE_DAY_HALF_AFTERNOON :
                        if (curTime.isBefore(endWorkTimeFm)) {
                            outTime = curTime;
                        } else {
                            outTime = endWorkTimeFm;
                        }
                        duration = Duration.between(inTime, outTime);
                        attend.updateWorkTime(duration.toHours(), duration.toMinutesPart());
                        break;
                    case ATTEND_CODE_WORK_PM :

                        break;
                    case ATTEND_CODE_DAY_HOLLY:
                        if(in != null && out != null) {
                            inTime = LocalDateTime.parse(date + in, ofPattern);
                            outTime = LocalDateTime.parse(date + out, ofPattern);

                            duration = Duration.between(inTime, outTime);

                            if(outTime.isAfter(startLunchTime) && outTime.isBefore(endLunchTime)){
                                outTime = startLunchTime;
                            }
                            if(outTime.isAfter(endLunchTime)){
                                duration.minusMinutes(60);
                            }

                            attend.updateWorkTime(duration.toHours(), duration.toMinutesPart());
                        } else {
                            attend.updateWorkTime(0, 0);
                        }
                        break;
                }
            } catch(Exception e){
                //throw new RuntimeException("");
            }
        }

        /*attendList.forEach(attend -> {
            String date = attend.getAttendDate();
            String in = attend.getInTime();
            String out = attend.getOutTime();

            LocalDateTime curTime = LocalDateTime.now();
            LocalDateTime startLunchTime = LocalDateTime.parse(date + TimeCode.TIME_CODE_START_LUNCH.getTimeCodeValue(), ofPattern);  // 점심 시작 시간
            LocalDateTime endLunchTime = LocalDateTime.parse(date + TimeCode.TIME_CODE_END_LUNCH.getTimeCodeValue(), ofPattern);    // 점심 종료 시간
            LocalDateTime startWorkTimeFm = LocalDateTime.parse(date + TimeCode.TIME_CODE_START_WORK.getTimeCodeValue(), ofPattern); // 정상 출근 시간
            LocalDateTime endWorkTimeFm = LocalDateTime.parse(date + TimeCode.TIME_CODE_END_WORK.getTimeCodeValue(), ofPattern);   // 정상 퇴근 시간

            Duration duration = null;
            LocalDateTime inTime = null;
            LocalDateTime outTime = null;

            AttendCode code = AttendCode.findByStatusCode(attend.getAttendCode());

            try{
                switch (code){
                    case ATTEND_CODE_DAY_NORMAL :
                    case ATTEND_CODE_DAY_HALF_MORNING :
                    case ATTEND_CODE_DAY_HALF_HALF_MORNING :
                    case ATTEND_CODE_DAY_HALF_HALF_AFTERNOON :
                    case ATTEND_CODE_WORK_HOME :
                        if(in != null && out != null) {
                            inTime = LocalDateTime.parse(date + in, ofPattern);
                            outTime = LocalDateTime.parse(date + out, ofPattern);
                        }
                        else if(in != null && out == null) {
                            inTime = LocalDateTime.parse(date + in, ofPattern);

                            if(curTime.isAfter(startLunchTime) && curTime.isBefore(endLunchTime)){
                                // 점심시간
                                outTime = startLunchTime;
                            } else if((curTime.isAfter(startWorkTimeFm) && curTime.isBefore(startLunchTime) || (curTime.isAfter(endLunchTime) && curTime.isBefore(endWorkTimeFm)))){
                                // 근무시간
                                outTime = LocalDateTime.parse(curTime.format(ofPattern), ofPattern);
                            } else if(curTime.isAfter(endWorkTimeFm)){
                                // 퇴근
                                outTime = endWorkTimeFm;
                            }
                        }
                        else if(in == null && out != null) {
                            // PM일 경우에만 고려

                        }

                        duration = Duration.between(inTime, outTime);
                        // 점심시간 근무시간에서 제외
                        if(curTime.isAfter(endLunchTime)){
                            duration.minusMinutes(60);
                        }

                        attend.updateWorkTime(duration.toHours(), duration.toMinutesPart());
                        break;
                    case ATTEND_CODE_DAY_HALF_AFTERNOON :
                        if (curTime.isBefore(endWorkTimeFm)) {
                            outTime = curTime;
                        } else {
                            outTime = endWorkTimeFm;
                        }
                        duration = Duration.between(inTime, outTime);
                        attend.updateWorkTime(duration.toHours(), duration.toMinutesPart());
                        break;
                    case ATTEND_CODE_WORK_PM :

                        break;
                    case ATTEND_CODE_DAY_HOLLY:
                        if(in != null && out != null) {
                            inTime = LocalDateTime.parse(date + in, ofPattern);
                            outTime = LocalDateTime.parse(date + out, ofPattern);

                            duration = Duration.between(inTime, outTime);

                            if(outTime.isAfter(startLunchTime) && outTime.isBefore(endLunchTime)){
                                outTime = startLunchTime;
                            }
                            if(outTime.isAfter(endLunchTime)){
                                duration.minusMinutes(60);
                            }

                            attend.updateWorkTime(duration.toHours(), duration.toMinutesPart());
                        } else {
                            attend.updateWorkTime(0, 0);
                        }
                        break;
                }
            } catch(Exception e){
                //throw new RuntimeException("");
            }
        });*/
    }
}