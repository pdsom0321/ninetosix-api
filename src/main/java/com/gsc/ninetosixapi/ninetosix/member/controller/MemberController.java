package com.gsc.ninetosixapi.ninetosix.member.controller;

import com.gsc.ninetosixapi.core.aspect.UserId;
import com.gsc.ninetosixapi.ninetosix.member.dto.*;
import com.gsc.ninetosixapi.ninetosix.member.service.MemberService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    @ApiOperation(value = "비밀번호 재설정(90일 이후), 비밀번호 찾기(변경)")
    @PutMapping("member/password")
    public ResponseEntity<String> changePassword(@RequestBody PasswordReqDTO reqDTO) {
        try {
            memberService.changePassword(reqDTO);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호를 확인하신 후 회원정보를 변경하세요.");
        }
    }

    @UserId
    @ApiOperation(value = "비밀번호 변경 다음에 하기", notes = "passwordExpiryDate 를 PASSWORD_EXPIRY_DAY(90일) 만큼 연장")
    @PutMapping("member/password-expiry")
    public ResponseEntity<Void> changePasswordExpiry(HttpServletRequest request) {
        long memberId = (long) request.getAttribute("memberId");
        memberService.changePasswordExpiry(memberId);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value = "로그아웃", notes = "refresh token 삭제")
    @PostMapping("member/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutReqDTO reqDTO) {
        memberService.logout(reqDTO);
        return ResponseEntity.ok().build();
    }

    @ApiOperation(value="토큰 재발급", notes = "AT, RT 재발급")
    @PostMapping("reissue")
    public ResponseEntity<ReissueResDTO> reissue(@RequestBody ReissueReqDTO reqDTO) {
        return ResponseEntity.ok(memberService.reissue(reqDTO));
    }

    @UserId
    @ApiOperation(value = "회원 정보. 마이 페이지")
    @GetMapping("my-page")
    public ResponseEntity<MyPageResDTO> myPage(HttpServletRequest request) {
        long memberId = (long) request.getAttribute("memberId");
        return ResponseEntity.ok(memberService.myPage(memberId));
    }

    @UserId
    @ApiOperation(value = "role 조회", notes = "ROLE_ADMIN(관리자), ROLE_MANAGER(팀장), ROLE_MEMBER(팀원)")
    @GetMapping("role")
    public ResponseEntity<List<String>> role(HttpServletRequest request) {
        long memberId = (long) request.getAttribute("memberId");
        return ResponseEntity.ok(memberService.role(memberId));
    }

    @UserId
    @ApiOperation(value = "회원탈퇴", notes = "member, member_role, attend 데이터 모두 삭제됨 -> 삭제 안하고 플래그(delete_yn) 변경할 예정 이므로 주석 처리 2023-10-17")
    @DeleteMapping("member/withdrawal")
    public ResponseEntity<Void> withdrawal(HttpServletRequest request) {
        long memberId = (long) request.getAttribute("memberId");
        memberService.withdrawal(memberId);
        return ResponseEntity.ok().build();
    }
}
