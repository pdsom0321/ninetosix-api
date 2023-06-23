package com.gsc.ninetosixapi.ninetosix.member.service;

import com.gsc.ninetosixapi.ninetosix.member.dto.GenerateCodeReqDTO;
import com.gsc.ninetosixapi.ninetosix.member.dto.VerifyCodeReqDTO;
import com.gsc.ninetosixapi.ninetosix.member.entity.AuthenticationCode;
import com.gsc.ninetosixapi.ninetosix.member.repository.AuthenticationCodeRepository;
import com.gsc.ninetosixapi.ninetosix.member.vo.AuthenticationCodeType;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationCodeService {
    private final AuthenticationCodeRepository authenticationCodeRepository;
    private final JavaMailSender javaMailSender;

    public void generateCode(GenerateCodeReqDTO reqDTO) {
        int code = generateRandomNumber();
        String title = getEmailTitle(reqDTO.type());
        String text = "귀하의 인증 번호는 " + code + " 입니다.";

        sendEmail(reqDTO.email(), title, text);
        saveAuthenticationCode(code, reqDTO.email(), reqDTO.type());
    }

    private int generateRandomNumber() {
        Random ran = new Random();
        return ran.nextInt(888888) + 111111;
    }

    private String getEmailTitle(String type) {
        AuthenticationCodeType authenticationCodeType = AuthenticationCodeType.valueOf(type);

        switch (authenticationCodeType) {
            case SIGNUP:
                return "회원가입 인증 번호 발급 안내 입니다.";
            case PASSWORD:
                return "비밀번호 인증 번호 발급 안내 입니다.";
            default:
                throw new IllegalArgumentException("Unsupported authentication code type: " + type);
        }
    }

    private void sendEmail(String email, String title, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(title);
        message.setText(text);
        javaMailSender.send(message);
    }

    private void saveAuthenticationCode(int code, String email, String type) {
        authenticationCodeRepository.save(AuthenticationCode.create(code, email, type));
    }

    public Boolean verifyCode(VerifyCodeReqDTO reqDTO) {
        AuthenticationCode authenticationCode = authenticationCodeRepository.findByCodeAndEmailAndTypeAndExpireDateGreaterThanAndExpired(reqDTO.code(), reqDTO.email(), AuthenticationCodeType.valueOf(reqDTO.type()), LocalDateTime.now(), false)
                .orElseThrow(() -> new NoSuchElementException("authentication code 정보가 없습니다."));

        authenticationCode.isDone();
        return true;
    }
}
