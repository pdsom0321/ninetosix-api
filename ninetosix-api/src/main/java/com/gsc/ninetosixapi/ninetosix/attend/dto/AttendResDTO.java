package com.gsc.ninetosixapi.ninetosix.attend.dto;

import com.gsc.ninetosixapi.ninetosix.attend.entity.Attend;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AttendResDTO {
    private List<Attend> attendList;
}
