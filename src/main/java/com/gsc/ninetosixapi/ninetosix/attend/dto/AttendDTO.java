package com.gsc.ninetosixapi.ninetosix.attend.dto;

import com.gsc.ninetosixapi.ninetosix.attend.entity.Attend;

public record AttendDTO(String attendDate, String inTime, String outTime, String workTime, Long memberId) {
    public static AttendDTO of(Attend attend) {
        return new AttendDTO(attend.getAttendDate().substring(6), attend.getInTime(), attend.getOutTime(), attend.getWorkTime(), attend.getMember().getId());
    }
}
