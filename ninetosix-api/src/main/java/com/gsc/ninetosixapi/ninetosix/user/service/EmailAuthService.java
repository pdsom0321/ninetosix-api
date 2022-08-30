package com.gsc.ninetosixapi.ninetosix.user.service;

import com.gsc.ninetosixapi.ninetosix.user.dto.EmailAuthRequestDTO;
import com.gsc.ninetosixapi.ninetosix.user.dto.UserInfoDTO;
import com.gsc.ninetosixapi.ninetosix.user.entity.EmailAuth;
import com.gsc.ninetosixapi.ninetosix.user.entity.User;
import com.gsc.ninetosixapi.ninetosix.user.repository.EmailAuthRepository;
import com.gsc.ninetosixapi.ninetosix.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@EnableAsync
@RequiredArgsConstructor
public class EmailAuthService {
    private final JavaMailSender javaMailSender;

    private final EmailAuthRepository emailAuthRepository;
    private final UserRepository userRepository;

    @Transactional
    public EmailAuth save(UserInfoDTO userInfoDTO) {
        return emailAuthRepository.save(
                EmailAuth.builder()
                        .email(userInfoDTO.getEmail())
                        .authToken(UUID.randomUUID().toString())
                        .expireDate(LocalDateTime.now().plusMinutes(5L))
                        .expired(false)
                        .build()
        );
    }

    @Transactional
    public EmailAuthRequestDTO confirmEmail(EmailAuthRequestDTO requestDTO){
        EmailAuth emailAuth = emailAuthRepository.findByEmailAndAuthTokenAndExpireDateGreaterThanAndExpired(requestDTO.getEmail(), requestDTO.getAuthToken(), LocalDateTime.now(), false)
                .orElseThrow(() -> new RuntimeException("email auth 정보가 없습니다."));
        User user = userRepository.findByEmail(requestDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("user 정보가 없습니다."));

        emailAuth.useToken(); // true
        user.emailVerifiedSuccess();

        return requestDTO;
    }

    @Async
    public void send(String email, String authToken) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("회원가입 이메일 인증");
        message.setText("http://localhost:8080/auth/signup/confirm-email?email="+email+"&authToken="+authToken);

        javaMailSender.send(message);
    }


}
