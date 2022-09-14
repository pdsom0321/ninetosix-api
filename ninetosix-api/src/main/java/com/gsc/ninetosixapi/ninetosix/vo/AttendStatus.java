package com.gsc.ninetosixapi.ninetosix.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum AttendStatus {
    ATTEND_STATUS_DAY_NORMAL("ST01", "정상출근")
    , ATTEND_STATUS_DAY_MORNING("ST02", "오전반차")
    , ATTEND_STATUS_DAY_AFTERNOON("ST03","오후반차")
    , ATTEND_STATUS_DAY_HALF_MORNING("ST04","오전반반차")
    , ATTEND_STATUS_DAY_HALF_AFTERNOON("ST005","오후반반차")
    , ATTEND_STATUS_DAY_HOLLY("ST06","휴가")
    , ATTEND_STATUS_WORK_HOME("ST07","재택근무")
    , ATTEND_STATUS_WORK_PM("ST08", "PM");

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
