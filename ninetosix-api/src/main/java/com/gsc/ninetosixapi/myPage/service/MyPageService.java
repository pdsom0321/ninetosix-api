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

    public MyPageDto getUserInfo(String email){
        Optional<User> user = userRepository.findByEmail(email);
        return MyPageDto.from(user);
    }

    public void modify(MyPageDto myPageDto){
        long id = 0L;
        Optional<User> userInfo = userRepository.findByEmail(myPageDto.getEmail());
        if(userInfo != null){

        }
    }
}
