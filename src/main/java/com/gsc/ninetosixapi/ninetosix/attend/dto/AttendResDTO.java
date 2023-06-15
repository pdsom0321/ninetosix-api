package com.gsc.ninetosixapi.ninetosix.attend.dto;

import com.gsc.ninetosixapi.ninetosix.attend.entity.Attend;

public record  AttendResDTO(String attendCode, String attendDate, String inTime, String outTime) {
    public static AttendResDTO of(Attend attend) {
        return new AttendResDTO(attend.getAttendCode(), attend.getAttendDate(), attend.getInTime(), attend.getOutTime());
    }
}