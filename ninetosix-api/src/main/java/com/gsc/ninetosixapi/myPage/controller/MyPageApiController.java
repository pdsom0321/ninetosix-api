package com.gsc.ninetosixapi.myPage.controller;

import com.gsc.ninetosixapi.myPage.dto.MyPageDto;
import com.gsc.ninetosixapi.myPage.service.MyPageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Api(tags = {"MyPage API"})
public class MyPageApiController {
    private final MyPageService myPageService;

    @PostMapping("/mypage")
    public ResponseEntity<?> getUserInfo(@RequestParam String email, Model model){
        MyPageDto result = myPageService.getUserInfo(email);
        return new ResponseEntity<MyPageDto>(result,HttpStatus.OK);
    }

    @PostMapping("/mypage/modify")
    @ApiOperation(value = "수정", response = MyPageApiController.class)
    public ResponseEntity<?> modify(@RequestBody MyPageDto myPageDto){
        myPageService.modify(myPageDto);
        return new ResponseEntity(HttpStatus.OK);
    }
}
