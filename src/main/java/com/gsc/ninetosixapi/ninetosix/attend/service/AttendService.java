package com.gsc.ninetosixapi.ninetosix.attend.service;

import com.gsc.ninetosixapi.core.jwt.MemberContext;
import com.gsc.ninetosixapi.ninetosix.attend.dto.*;
import com.gsc.ninetosixapi.ninetosix.attend.entity.Attend;
import com.gsc.ninetosixapi.ninetosix.attend.repository.AttendRepository;
import com.gsc.ninetosixapi.ninetosix.member.entity.Member;
import com.gsc.ninetosixapi.ninetosix.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AttendService {
    private final AttendRepository attendRepository;
    private final MemberService memberService;

    public void onWork(OnWorkReqDTO reqDTO) {
        Long memberId = MemberContext.getMemberId();
        Member member = memberService.findById(memberId);

        attendRepository.save(Attend.createAttend(getCurrentDate(), getCurrentTime(), reqDTO.attendCode(), reqDTO.locationCode(), member));
    }

    public void onWorkDuringDayOff(OnWorkDuringDayOffReqDTO reqDTO) {
        Long memberId = MemberContext.getMemberId();

        Attend attend = attendRepository.findByMemberIdAndAttendDate(memberId, getCurrentDate())
                .orElseThrow(() -> new NoSuchElementException("attend 정보가 없습니다."));

        attend.updateInTimeAndLocationCode(getCurrentTime(), reqDTO.locationCode());
    }

    public void offWork() {
        Long memberId = MemberContext.getMemberId();

        Attend attend = attendRepository.findByMemberIdAndAttendDate(memberId, getCurrentDate())
                .orElseThrow(() -> new NoSuchElementException("attend 정보가 없습니다."));

        attend.updateOutTimeAndWorkTime(getCurrentTime());
    }

    public void dayOff(String attendCode, AttendCodeReqDTO reqDTO) {
        int from = reqDTO.from();
        int to = reqDTO.to();
        Long memberId = MemberContext.getMemberId();
        Member member = memberService.findById(memberId);

        IntStream.rangeClosed(from, to)
                .forEach(current -> {
                    Attend attend = attendRepository.findByMemberIdAndAttendDate(memberId, String.valueOf(current))
                            .orElse(Attend.createAttendDayOff(String.valueOf(current), attendCode, member));

                    attend.updateCode(attendCode);
                    attendRepository.save(attend);
                });
    }

    public AttendResDTO attendInfo(long memberId) {
        Attend attend = attendRepository.findByMemberIdAndAttendDate(memberId, getCurrentDate())
                .orElseGet(Attend::new);

        return AttendResDTO.of(attend);
    }

    public List<AttendResDTO> yesterdayAndTodayAttendanceList(Long memberId) {
        LocalDateTime now = LocalDateTime.now();
        String startDate = now.minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String endDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        List<String> dayList = List.of(startDate, endDate);
        return dayList.stream()
                .sorted()
                .map(day -> attendRepository.findByMemberIdAndAttendDate(memberId, day)
                        .orElseGet(Attend::new))
                .map(AttendResDTO::of)
                .collect(Collectors.toList());
    }

    public List<MonthlyResDTO> monthlyAttendanceList(Long memberId, String month) {
        List<Attend> attendList = attendRepository.findByMemberIdAndAttendDateContainsOrderByAttendDateAsc(memberId, month);

        return attendList.stream()
                // .filter(attend -> Objects.nonNull(attend.getInTime()))
                .map(MonthlyResDTO::of)
                .collect(Collectors.toList());
    }

    public List<Integer> getDayOfMonth(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        int lastDayOfMonth = yearMonth.lengthOfMonth();

        return IntStream.rangeClosed(1, lastDayOfMonth)
                .mapToObj(yearMonth::atDay)
                .map(LocalDate::getDayOfMonth)
                .collect(Collectors.toList());
    }

    public List<ExportDTO> getAttends(int year, int month) {
        return memberService.findAll().stream()  // TODO: member 조회 시 회사, 부서 또는 팀 조건 필요 (우선 모든 member 가져오는 조건으로 개발)
                .map(member -> {
                    List<AttendDTO> list = monthlyMembersAttendanceListForExport(member.getId(), String.format("%04d%02d", year, month));
                    return new ExportDTO(member.getName(), list);
                })
                .sorted(Comparator.comparing(ExportDTO::memberName).reversed()) // TEST: 이름 내림차순으로 정렬
                .toList();
    }

    public List<AttendDTO> monthlyMembersAttendanceListForExport(Long memberId, String month) {
        return attendRepository.findByMemberIdAndAttendDateStartsWith(memberId, month).stream()
                .map(AttendDTO::of)
                .collect(Collectors.toList());
    }

    private String getCurrentDate() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    private String getCurrentTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("HHmm"));
    }

    /**
     * 1. 11시 30분 ~ 12시 30분 점심시간시 근무시간에서 제외
     * 2.
     **/
    /*public void calWorkTime(List<Attend> attendList){
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
    }*/
}