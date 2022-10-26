package com.gsc.ninetosixapi.ninetosix.vo;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum AttendCode {
    ATTEND_CODE_DAY_NORMAL("AC01", "정상출근")
    , ATTEND_CODE_DAY_MORNING("AC02", "오전반차")
    , ATTEND_CODE_DAY_AFTERNOON("AC03","오후반차")
    , ATTEND_CODE_DAY_HALF_MORNING("AC04","오전반반차")
    , ATTEND_CODE_DAY_HALF_AFTERNOON("AC05","오후반반차")
    , ATTEND_CODE_DAY_HOLLY("AC06","휴가")
    , ATTEND_CODE_WORK_HOME("AC07","재택근무")
    , ATTEND_CODE_WORK_PM("AC08", "PM");

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
