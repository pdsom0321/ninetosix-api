package com.gsc.ninetosixapi.user.service;

import com.gsc.ninetosixapi.user.dto.UserInfoDTO;
import com.gsc.ninetosixapi.user.entity.User;
import com.gsc.ninetosixapi.user.repository.UserRepository;
import com.gsc.ninetosixapi.vo.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public void join(UserInfoDTO userInfoDTO) {
        Optional<User> user = userRepository.findByEmail(userInfoDTO.getEmail());

        if(!user.isPresent()) {
            User newUser = new User(
                    userInfoDTO.getEmail(),
                    userInfoDTO.getName(),
                    passwordEncoder.encode(userInfoDTO.getPwd()),
                    userInfoDTO.getContact(),
                    userInfoDTO.getEmpNo(),
                    userInfoDTO.getDeptCd(),
                    userInfoDTO.getCompCd(),
                    userInfoDTO.getPushAgreeYN(),
                    Role.ROLE_ADMIN.name()
            );
            userRepository.save(newUser);
        } else {
            throw new DuplicateKeyException("중복된 이메일 입니다.");
        }
    }

}
