package com.gsc.ninetosixapi.ninetosix.member.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

public record SignupReqDTO(
        @NotBlank(message = "이메일은 필수 입력 값 입니다.")
        @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
        String email,
        @NotBlank(message = "이름은 필수 입력 값 입니다.")
        String name,
        @NotBlank(message = "비밀번호는 필수 입력 값 입니다.")
        @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}", message = "비밀번호는 영문, 숫자, 특수문자를 사용하여 8자리 이상 입력해주세요.")
        String password,
        @NotBlank(message = "연락처는 필수 입력 값 입니다.")
        String contact,
        String companyCode,
        String pushAgreeYn
) {
}
