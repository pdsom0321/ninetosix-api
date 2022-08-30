package com.gsc.ninetosixapi.ninetosix.user.controller;

import com.gsc.ninetosixapi.ninetosix.user.dto.*;
import com.gsc.ninetosixapi.ninetosix.user.service.AuthService;
import com.gsc.ninetosixapi.ninetosix.user.service.EmailAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final EmailAuthService emailAuthService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO> signup(@RequestBody UserInfoDTO userInfoDTO) {
        return ResponseEntity.ok(authService.signup(userInfoDTO));
    }

    @GetMapping("/signup/confirm-email")
    public ResponseEntity<EmailAuthRequestDTO> confirmEmail(@ModelAttribute EmailAuthRequestDTO requestDTO) {
        System.out.println("####################### " + requestDTO.getEmail() + "  " + requestDTO.getAuthToken());
        return ResponseEntity.ok(emailAuthService.confirmEmail(requestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody UserRequestDTO userRequestDTO) {
        return ResponseEntity.ok(authService.login(userRequestDTO));
    }

    @PostMapping("/reissue")
    public ResponseEntity<TokenDTO> reissue(@RequestBody TokenRequestDTO tokenRequestDTO) {
        return ResponseEntity.ok(authService.reissue(tokenRequestDTO));
    }

}
