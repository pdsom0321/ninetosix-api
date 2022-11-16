package com.gsc.ninetosixapi.ninetosix.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum TimeCode {
    TIME_CODE_START_WORK_NORMAL("TC01", "090000")
    , TIME_CODE_END_WORK_NORMAL("TC02", "180000")
    , TIME_CODE_START_WORK_HALF_MORNING("TC03", "140000")
    , TIME_CODE_END_WORK_HALF_MORNING("TC04", "140000")
    , TIME_CODE_START_WORK_HALF_AFTERNOON("TC05", "140000")
    , TIME_CODE_END_WORK_HALF_AFTERNOON("TC06", "140000")
    , TIME_CODE_START_LUNCH("TC03", "113000")
    , TIME_CODE_END_LUNCH("TC04", "123000")
    , TIME_CODE_DAY_MIDNIGHT("TC05", "000000");

    private String timeCodeName;
    private String timeCodeValue;

    public static TimeCode findByTimeCode(String timeCodeValue){
        return Arrays.stream(TimeCode.values())
                .filter(timeCode -> timeCode.getTimeCodeValue().equals(timeCodeValue))
                .findAny()
                .orElse(null);
    }

}
