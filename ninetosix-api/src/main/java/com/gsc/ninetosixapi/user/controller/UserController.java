package com.gsc.ninetosixapi.user.controller;

import com.gsc.ninetosixapi.user.dto.UserInfoDTO;
import com.gsc.ninetosixapi.user.service.UserService;
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
@RequestMapping("/user")
@Api(tags = {"User API"})
public class UserController {

    private final UserService userService;

    @PostMapping("/new")
    @ApiOperation(value = "회원가입", response = UserController.class)
    public ResponseEntity<Object> create(@RequestBody UserInfoDTO userInfoDTO) {
        userService.create(userInfoDTO);
        return new ResponseEntity(HttpStatus.OK);
    }
}
