package com.gsc.ninetosixapi.ninetosix.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
/**
 * TC01 정상출근
 * TC02 정상퇴근
 * TC03 오전반차 시작
 * TC04 오전반차 종료
 * TC05 오후반차 시작
 * TC06 오후반차 종료
 * TC07 오전반반차 시작
 * TC08 오전반반차 종료
 * TC09 오후반반차 시작
 * TC10 오후반반차 종료
 * TC11 점심시간 시작
 * TC12 점심시간 종료
 * TC13 자정
 * */
@Getter
@AllArgsConstructor
public enum TimeCode {
    TIME_CODE_START_WORK_NORMAL("TC01", "090000")
    , TIME_CODE_END_WORK_NORMAL("TC02", "180000")
    , TIME_CODE_START_WORK_HALF_MORNING("TC03", "090000")
    , TIME_CODE_END_WORK_HALF_MORNING("TC04", "140000")
    , TIME_CODE_START_WORK_HALF_AFTERNOON("TC05", "140000")
    , TIME_CODE_END_WORK_HALF_AFTERNOON("TC06", "180000")
    , TIME_CODE_START_WORK_QUARTER_MORNING("TC07", "110000")
    , TIME_CODE_END_WORK_QUARTER_MORNING("TC08", "180000")
    , TIME_CODE_START_WORK_QUARTER_AFTERNOON("TC09", "090000")
    , TIME_CODE_END_WORK_QUARTER_AFTERNOON("TC10", "140000")
    , TIME_CODE_START_LUNCH("TC11", "113000")
    , TIME_CODE_END_LUNCH("TC12", "123000")
    , TIME_CODE_DAY_MIDNIGHT("TC13", "000000");

    private String timeCodeName;
    private String timeCodeValue;

    public static TimeCode findByTimeCode(String timeCodeValue){
        return Arrays.stream(TimeCode.values())
                .filter(timeCode -> timeCode.getTimeCodeValue().equals(timeCodeValue))
                .findAny()
                .orElse(null);
    }

    public static TimeCode findByTimeName(String timeCodeName){
        return Arrays.stream(TimeCode.values())
                .filter(timeCode -> timeCode.getTimeCodeName().equals(timeCodeName))
                .findAny()
                .orElse(null);
    }
}
