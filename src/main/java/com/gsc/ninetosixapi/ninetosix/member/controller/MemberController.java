package com.gsc.ninetosixapi.ninetosix.member.controller;

import com.gsc.ninetosixapi.ninetosix.member.dto.*;
import com.gsc.ninetosixapi.ninetosix.member.service.MemberService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @ApiOperation(value = "로그인")
    @PostMapping("login")
    public ResponseEntity<LoginResDTO> login(@RequestBody LoginReqDTO reqDTO) {
        return ResponseEntity.ok(memberService.login(reqDTO));
    }

    @ApiOperation(value = "회원가입")
    @PostMapping("member")
    public ResponseEntity<SignupResDTO> signup(@RequestBody SignupReqDTO reqDTO) {
        return ResponseEntity.ok(memberService.signup(reqDTO));
    }

    @ApiOperation(value = "비밀번호 변경")
    @PutMapping("member/password")
    public ResponseEntity<Void> changePassword(@RequestBody PasswordReqDTO reqDTO) {
        memberService.changePassword(reqDTO);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "비밀번호 변경 다음에 하기", notes = "passwordExpiryDate 를 PASSWORD_EXPIRY_DAY(90일) 만큼 연장")
    @PutMapping("member/password-expiry")
    public ResponseEntity<Void> changePasswordExpiry() {
        memberService.changePasswordExpiry();
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "로그아웃", notes = "refresh token 삭제")
    @PostMapping("member/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutReqDTO reqDTO) {
        memberService.logout(reqDTO);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "회원탈퇴", notes = "member, member_role, attend 데이터 모두 삭제됨")
    @DeleteMapping("member/withdrawal")
    public ResponseEntity<Void> withdrawal() {
        memberService.withdrawal();
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value="AT 토큰 재발급")
    @PostMapping("reissue")
    public ResponseEntity<ReissueResDTO> reissue(@RequestBody ReissueReqDTO reqDTO) {
        return ResponseEntity.ok(memberService.reissue(reqDTO));
    }

    @ApiOperation(value = "회원 정보. 마이페이지")
    @GetMapping("my-page")
    public ResponseEntity<MyPageResDTO> myPage() {
        return ResponseEntity.ok(memberService.myPage());
    }
}
