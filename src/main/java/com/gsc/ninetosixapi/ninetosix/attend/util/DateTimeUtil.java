package com.gsc.ninetosixapi.ninetosix.attend.util;

import com.gsc.ninetosixapi.ninetosix.vo.TimeCode;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Slf4j
public class DateTimeUtil {
    public LocalDateTime calWorkTime(String date, String time, String strPattern){
        DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern(strPattern, Locale.KOREA);
        return LocalDateTime.parse(date + time, ofPattern);
    }

    public LocalDateTime getWorkTime(String date, String timeCodeName, String strPattern){
        DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern(strPattern, Locale.KOREA);
        TimeCode timeCode = TimeCode.findByTimeName(timeCodeName);
        return LocalDateTime.parse(date + timeCode.getTimeCodeValue(), ofPattern);
    }
}
