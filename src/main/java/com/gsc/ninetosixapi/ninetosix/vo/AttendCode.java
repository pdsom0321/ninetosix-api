package com.gsc.ninetosixapi.ninetosix.vo;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum AttendCode {
    ATTEND_CODE_DAY_NORMAL("AT01", "정상출근")
    , ATTEND_CODE_DAY_MORNING("AT02", "오전반차")
    , ATTEND_CODE_DAY_AFTERNOON("AT03","오후반차")
    , ATTEND_CODE_DAY_HALF_MORNING("AT04","오전반반차")
    , ATTEND_CODE_DAY_HALF_AFTERNOON("AT05","오후반반차")
    , ATTEND_CODE_DAY_HOLLY("AT06","휴가")
    , ATTEND_CODE_WORK_HOME("AT07","재택근무")
    , ATTEND_CODE_WORK_PM("AT08", "PM");

    private final String attendCode;
    private final String attendCodeName;

    AttendCode(String attendCode, String attendCodeName){
        this.attendCode = attendCode;
        this.attendCodeName = attendCodeName;
    }

    public static AttendCode findByStatusCode(String attendCodeCode){
        return Arrays.stream(AttendCode.values())
                .filter(attendCode -> attendCode.getAttendCode().equals(attendCodeCode))
                .findAny()
                .orElse(null);
    }
}
