package com.gsc.ninetosixapi.ninetosix.user.service;

import com.gsc.ninetosixapi.ninetosix.user.dto.CodeCheckReqDTO;
import com.gsc.ninetosixapi.ninetosix.user.dto.CodeSendReqDTO;
import com.gsc.ninetosixapi.ninetosix.user.entity.AuthCode;
import com.gsc.ninetosixapi.ninetosix.user.repository.AuthCodeRepository;
import com.gsc.ninetosixapi.ninetosix.user.vo.AuthCodeFrom;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@EnableAsync
@RequiredArgsConstructor
public class AuthCodeService {
    private final JavaMailSender javaMailSender;

    private final AuthCodeRepository authCodeRepository;

    public void sendCode(CodeSendReqDTO reqDTO) {
        String email = reqDTO.getEmail();
        String from = reqDTO.getFromType();

        String subject = "";
        if(from.equals(AuthCodeFrom.SIGNUP.name())) {
            subject = "회원가입 인증 번호 발급 안내 입니다.";
        } else if(from.equals(AuthCodeFrom.PASSWORD.name())) {
            subject = "비밀번호 인증 번호 발급 안내 입니다.";
        }

        Random ran = new Random();
        String ranCode = String.valueOf(ran.nextInt(888888) + 111111);   // 범위 : 111111 ~ 999999
        String text = "귀하의 인증 번호는 " + ranCode + " 입니다.";

        // 이메일 발송
        send(email, subject, text);

        // DB에 저장
        save(email, from, ranCode);
    }

    @Async
    public void send(String email, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText(text);

        javaMailSender.send(message);
    }

    @Transactional
    public AuthCode save(String email, String from, String ranCode) {
        return authCodeRepository.save(AuthCode.createAuthCode(email, from, ranCode));
    }

    public Boolean checkCode(CodeCheckReqDTO reqDTO) {
        AuthCode authCode = authCodeRepository.findByEmailAndRanCodeAndFromTypeAndExpireDateGreaterThanAndExpired(reqDTO.getEmail(), reqDTO.getRanCode(), AuthCodeFrom.valueOf(reqDTO.getFromType()), LocalDateTime.now(), false)
                .orElseThrow(() -> new RuntimeException("code 유저 정보가 없습니다."));

        // 인증 TRUE 업데이트
        authCode.isTrue();
        authCodeRepository.save(authCode);
        return true;
    }


}
