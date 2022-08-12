package com.gsc.ninetosixapi.user.service;

import com.gsc.ninetosixapi.user.dto.UserInfoDTO;
import com.gsc.ninetosixapi.user.entity.User;
import com.gsc.ninetosixapi.user.repository.UserRepository;
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

    public void create(UserInfoDTO userInfoDTO) {
        Optional<User> user = userRepository.findByEmail(userInfoDTO.getEmail());

        if(!user.isPresent()) {
            userRepository.save(User.createUser(userInfoDTO, passwordEncoder));
        } else {
            throw new DuplicateKeyException("중복된 이메일 입니다.");
        }
    }

}
