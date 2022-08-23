package com.gsc.ninetosixapi.ninetosix.user.service;

import com.gsc.ninetosixapi.core.util.SecurityUtil;
import com.gsc.ninetosixapi.ninetosix.user.dto.UserResponseDTO;
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
    public UserResponseDTO getMemberInfo(String email) {
        return userRepository.findByEmail(email)
                .map(UserResponseDTO::of)
                .orElseThrow(() -> new RuntimeException("유저 정보가 없습니다."));
    }

    // 현재 SecurityContext 에 있는 유저 정보 가져오기
    @Transactional(readOnly = true)
    public UserResponseDTO getMyInfo() {
        return userRepository.findByEmail(SecurityUtil.getCurrentUserId())
                .map(UserResponseDTO::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));
    }

    /*public void create(UserInfoDTO userInfoDTO) {
        if(userRepository.findByEmail(userInfoDTO.getEmail()).isEmpty()) {
            Company company = companyService.getCompany(userInfoDTO.getCompanyCode());
            User user = userRepository.save(User.createUser(userInfoDTO, company, passwordEncoder));
            userRoleRepository.save(UserRole.createUserRole(user));
        } else {
            throw new DuplicateKeyException("중복된 이메일 입니다.");
        }
    }*/

}
