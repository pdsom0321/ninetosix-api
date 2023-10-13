package com.gsc.ninetosixapi.ninetosix.attend.dto;

import com.gsc.ninetosixapi.ninetosix.attend.entity.Attend;

public record AttendDTO(String attendDate, String attendCode, String inTime, String outTime, Long workTime, long memberId) {
    public static AttendDTO of(Attend attend) {
        return new AttendDTO(attend.getAttendDate().substring(6), attend.getAttendCode(), attend.getInTime(), attend.getOutTime(), attend.getWorkTime(), attend.getMember().getId());
    }
}
