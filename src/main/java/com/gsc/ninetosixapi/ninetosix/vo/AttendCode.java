package com.gsc.ninetosixapi.ninetosix.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AttendCode {
    DAY("AT01", "정상출근")
    , HALF_MORNING("AT02", "오전반차")
    , HALF_AFTERNOON("AT03","오후반차")
    , QUARTER_MORNING("AT04","오전반반차")
    , QUARTER_AFTERNOON("AT05","오후반반차")
    , HOLIDAY("AT06","휴가")
    , HOME("AT07","재택근무")
    , PM("AT08", "PM");

    private final String attendCode;
    private final String attendCodeName;
}
