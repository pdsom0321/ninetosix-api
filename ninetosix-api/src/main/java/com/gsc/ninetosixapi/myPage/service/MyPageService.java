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
        // TODO : MyPageDto 코드 정리 필요
        Optional<User> user = userRepository.findByEmail(user_email);
        return MyPageDto.from(user);
    }

    public long modify(String user_email){
        Optional<User> userInfo = userRepository.findByEmail(user_email);
        return userInfo.get().getId();
    }
}
