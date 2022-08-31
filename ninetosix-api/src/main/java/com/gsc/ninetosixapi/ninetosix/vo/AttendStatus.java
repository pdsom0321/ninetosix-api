package com.gsc.ninetosixapi.ninetosix.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum AttendStatus {
    ATTEND_STATUS_DAY_NORMAL("ST001", "정상출근")
    , ATTEND_STATUS_DAY_MORNING("ST002", "오전반차")
    , ATTEND_STATUS_DAY_AFTERNOON("ST003","오후반차")
    , ATTEND_STATUS_DAY_HALF_MORNING("ST004","오전반반차")
    , ATTEND_STATUS_DAY_HALF_AFTERNOON("ST005","오후반반차")
    , ATTEND_STATUS_WORK_HOME("ST006","재택근무")
    , ATTEND_STATUS_DAY_HOLLY("ST007","휴가");

    private final String attendStatusCode;
    private final String attendStatusName;

    AttendStatus(String attendStatusCode, String attendStatusName){
        this.attendStatusCode = attendStatusCode;
        this.attendStatusName = attendStatusName;
    }

    public static AttendStatus findByStatusCode(String attendStatusCode){
        return Arrays.stream(AttendStatus.values())
                .filter(statusCode -> statusCode.getAttendStatusCode().equals(attendStatusCode))
                .findAny()
                .orElse(null);
    }
}
