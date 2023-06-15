package com.gsc.ninetosixapi.ninetosix.attend.service;

import com.gsc.ninetosixapi.core.jwt.MemberContext;
import com.gsc.ninetosixapi.ninetosix.attend.dto.AttendCodeReqDTO;
import com.gsc.ninetosixapi.ninetosix.attend.dto.AttendOnReqDTO;
import com.gsc.ninetosixapi.ninetosix.attend.dto.AttendReqDTO;
import com.gsc.ninetosixapi.ninetosix.attend.dto.AttendResDTO;
import com.gsc.ninetosixapi.ninetosix.attend.entity.Attend;
import com.gsc.ninetosixapi.ninetosix.attend.repository.AttendRepository;
import com.gsc.ninetosixapi.ninetosix.attend.util.DateTimeUtil;
import com.gsc.ninetosixapi.ninetosix.member.entity.Member;
import com.gsc.ninetosixapi.ninetosix.member.service.AuthService;
import com.gsc.ninetosixapi.ninetosix.vo.AttendCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AttendService {
    private final AttendRepository attendRepository;
    private final AuthService authService;
    private final DateTimeUtil dateTimeUtil;

     /* TODO :
            * 출근하기 -> INSERT
                    -> UPDATE intime
              퇴근하기  -> UPDATE outtime
            * 유효성 체크는 service가 아닌 controller에서 !!
              controller에서는 @valid, dto에서 @notBlank같은~
            * return ResponseEntity는 controller에서만 !!
            * List<Attend> return은 필요한 데이터만 resDTO로 !!
            * spring security Principal -> token의 payload 안의 id(member_id)로 attend 바로 조회해오기 jpa 아니면 jpql
         */

    public void onWork(AttendOnReqDTO reqDTO) {
        LocalDateTime now = LocalDateTime.now();
        String ymd = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String time = now.format(DateTimeFormatter.ofPattern("HHmm"));

        String attendCode = reqDTO.attendCode();
        String locationCode = reqDTO.locationCode();

        Long memberId = MemberContext.getMemberId();
        Member member = authService.findMemberById(memberId);

        attendRepository.save(Attend.createAttend(ymd, time, attendCode, locationCode, member));
   }

   public void offWork() {
       LocalDateTime now = LocalDateTime.now();
       String ymd = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
       String time = now.format(DateTimeFormatter.ofPattern("HHmm"));

       Long memberId = MemberContext.getMemberId();

       Optional.ofNullable(attendRepository.findByMemberIdAndAttendDate(memberId, ymd))
                       .ifPresent(attend -> attend.updateOutTime(time));
   }

    /*public void processAttendance(String email, AttendReqDTO reqDTO){
        String ymd = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String inTime = reqDTO.inTime();
        String outTime = reqDTO.outTime();
        String attendCode = reqDTO.attendCode();
        String locationCode = reqDTO.locationCode();
        Member member = authService.getMember(email);

       Optional.ofNullable(attendRepository.findByMemberAndAttendDate(member, ymd))
                .ifPresentOrElse(attend -> {
                            attend.updateCode(member, attendCode);

                            Optional.ofNullable(inTime).ifPresent(time -> attend.updateInTime(inTime));
                            Optional.ofNullable(outTime).ifPresent(time -> attend.updateOutTime(outTime));
                        },
                        () -> attendRepository.save(Attend.createAttend(ymd, locationCode, member, attendCode, inTime, outTime)));
    }*/

    public void processAttendanceByCode(String email, AttendCodeReqDTO reqDTO) {
        String code = reqDTO.attendCode();
        int from = reqDTO.from();
        int to = reqDTO.to();
        log.info("================from : " + from);
        log.info("================to : " + to);

        int current = from;
        while(current <= to) {
            log.info("================current : " + current);
            Member member = authService.getMember(email);

            int finalCurrent = current;
            Optional.ofNullable(attendRepository.findByMemberAndAttendDate(member, String.valueOf(current)))
                    .ifPresentOrElse(attend -> attend.updateCode(member, code),
                            () -> attendRepository.save(Attend.createAttendByCode(String.valueOf(finalCurrent), member, code)));
            current ++;
        }

//        return new ResponseEntity(HttpStatus.OK);
    }

    public List<AttendResDTO> getPreviousAndCurrentAttendanceList(Long memberId){
        LocalDateTime now = LocalDateTime.now();
        String startDate = now.minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String endDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

//        Member member = authService.getMember(email);
//        List<Attend> attendList = attendRepository.findTop2ByMemberAndAttendDateBetweenOrderByAttendDateAsc(member, startDate, endDate);
        List<Attend> attendList = attendRepository.findTop2ByMemberIdAndAttendDateBetweenOrderByAttendDateAsc(memberId, startDate, endDate);

        attendList = Optional.ofNullable(attendList)
                .filter(list -> list.size() == 1 && endDate.equals(list.get(0).getAttendDate()))
                .map(list -> {
                    list.add(0, new Attend());
                    return list;
                })
                .orElse(attendList);

        for (int i = attendList.size(); i < 2; i++) {
            attendList.add(new Attend());
        }

        // return attendList;
        return attendList.stream()
                .map(AttendResDTO::of)
                .collect(Collectors.toList());
    }

    public List<AttendResDTO> getMonthlyAttendanceList(Long memberId, String month){
        List<Attend> attendList = attendRepository.findByMemberIdAndAttendDateContainsOrderByAttendDateAsc(memberId, month);
        calWorkTime(attendList);
        return attendList.stream()
                .map(AttendResDTO::of)
                .collect(Collectors.toList());
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