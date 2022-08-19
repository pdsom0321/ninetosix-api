package com.gsc.ninetosixapi.ninetosix.user.controller;

import com.gsc.ninetosixapi.ninetosix.user.dto.UserResponseDTO;
import com.gsc.ninetosixapi.ninetosix.user.service.UserService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user/v1")
@Api(tags = {"User API"})
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getMyMemberInfo() {
        return ResponseEntity.ok(userService.getMyInfo());
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserResponseDTO> getMemberInfo(@PathVariable String email) {
        return ResponseEntity.ok(userService.getMemberInfo(email));
    }

    /*@PostMapping("/user")
    @ApiOperation(value = "회원가입", response = UserController.class)
    public ResponseEntity<Object> create(@RequestBody UserInfoDTO userInfoDTO) {
        userService.create(userInfoDTO);

        return new ResponseEntity(HttpStatus.OK);
    }*/
}
