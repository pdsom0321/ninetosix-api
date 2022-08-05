package com.gsc.ninetosixapi.myPage.dto;

import com.gsc.ninetosixapi.user.dto.UserInfoDTO;
import com.gsc.ninetosixapi.user.entity.User;
import lombok.*;

import java.util.Optional;

@Getter @Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MyPageDto {
        private String email;
        private String name;
        private String contact;
        private String empNo;
        private String compCd;
        private String deptCd;
        private String pushAgreeYN;

        public MyPageDto(String name, String email) {
                this.name = name;
                this.email = email;
        }

        public static MyPageDto from(Optional<User> user) {
                return new MyPageDto(
                        user.get().getName(),
                        user.get().getEmail());
        }
}
