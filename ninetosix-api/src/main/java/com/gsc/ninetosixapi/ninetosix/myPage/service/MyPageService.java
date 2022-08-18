package com.gsc.ninetosixapi.ninetosix.myPage.service;

import com.gsc.ninetosixapi.ninetosix.myPage.dto.MyPageDto;
import com.gsc.ninetosixapi.ninetosix.user.entity.User;
import com.gsc.ninetosixapi.ninetosix.user.repository.UserRepository;
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
            // TODO [MyPage] 수정 로직 개발 필요
            // userInfo.get().setName(myPageDto.getName());
        }
    }
}
