package com.gsc.ninetosixapi.myPage.service;

import com.gsc.ninetosixapi.myPage.dto.MyPageDto;
import com.gsc.ninetosixapi.user.dto.UserInfoDTO;
import com.gsc.ninetosixapi.user.entity.User;
import com.gsc.ninetosixapi.user.repository.UserRepository;
import com.gsc.ninetosixapi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final UserRepository userRepository;

    public MyPageDto init(String user_email){
        Optional<User> user = userRepository.findByEmail(user_email);
        return new MyPageDto(
                user.get().getEmail()
                , user.get().getName()
                , user.get().getContact()
                , user.get().getEmpNo()
                , user.get().getCompCd()
                , user.get().getDeptCd()
                , user.get().getPushAgreeYn()
        );
    }

    public long modify(String user_email){
        Optional<User> userInfo = userRepository.findByEmail(user_email);
        return userInfo.get().getId();
    }
}
