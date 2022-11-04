package com.gsc.ninetosixapi.ninetosix.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum TimeCode {
    TIME_CODE_START_WORK("TC01", "090000")
    , TIME_CODE_END_WORK("TC02", "180000")
    , TIME_CODE_START_LUNCH("TC03", "113000")
    , TIME_CODE_END_LUNCH("TC04", "123000");

    private String timeCodeName;
    private String timeCodeValue;

    public static TimeCode findByTimeCode(String timeCodeValue){
        return Arrays.stream(TimeCode.values())
                .filter(timeCode -> timeCode.getTimeCodeValue().equals(timeCodeValue))
                .findAny()
                .orElse(null);
    }

}
