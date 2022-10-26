package com.gsc.ninetosixapi.ninetosix.attend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AttendReqDTO {
    private String inTime;
    private String outTime;
    private String attendCode;
    private String locationCode;
}
