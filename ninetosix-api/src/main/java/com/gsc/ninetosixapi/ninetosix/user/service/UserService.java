package com.gsc.ninetosixapi.ninetosix.user.service;

import com.gsc.ninetosixapi.core.util.SecurityUtil;
import com.gsc.ninetosixapi.ninetosix.user.dto.UserResDTO;
import com.gsc.ninetosixapi.ninetosix.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserResDTO getMemberInfo(String email) {
        return userRepository.findByEmail(email)
                .map(UserResDTO::of)
                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));
    }

    // 현재 SecurityContext 에 있는 유저 정보 가져오기
    @Transactional(readOnly = true)
    public UserResDTO getMyInfo() {
        return userRepository.findByEmail(SecurityUtil.getCurrentUserId())
                .map(UserResDTO::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));
    }
}
