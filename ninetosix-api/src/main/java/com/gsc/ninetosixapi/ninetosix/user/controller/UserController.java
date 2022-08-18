package com.gsc.ninetosixapi.ninetosix.user.controller;

import com.gsc.ninetosixapi.ninetosix.user.service.UserService;
import com.gsc.ninetosixapi.ninetosix.user.dto.UserInfoDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/vi")
@Api(tags = {"User API"})
public class UserController {

    private final UserService userService;

    @PostMapping("/user")
    @ApiOperation(value = "회원가입", response = UserController.class)
    public ResponseEntity<Object> create(@RequestBody UserInfoDTO userInfoDTO) {
        userService.create(userInfoDTO);
        return new ResponseEntity(HttpStatus.OK);
    }
}
