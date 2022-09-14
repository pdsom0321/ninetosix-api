package com.gsc.ninetosixapi.ninetosix.attend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AttendReqDTO {
    private String email;
    private String attendStatus;
    private Long companyLocationId;
}
