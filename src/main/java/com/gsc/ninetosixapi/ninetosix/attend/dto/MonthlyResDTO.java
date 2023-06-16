package com.gsc.ninetosixapi.ninetosix.attend.dto;

import com.gsc.ninetosixapi.ninetosix.attend.entity.Attend;

public record MonthlyResDTO(String attendCode, String attendDate, String inTime, String outTime, String workTime) {
    public static MonthlyResDTO of(Attend attend) {
        return new MonthlyResDTO(attend.getAttendCode(), attend.getAttendDate(), attend.getInTime(), attend.getOutTime(), attend.getWorkTime());
    }
}
