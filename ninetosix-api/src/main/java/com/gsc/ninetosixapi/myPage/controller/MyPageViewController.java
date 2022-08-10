package com.gsc.ninetosixapi.myPage.controller;

import com.gsc.ninetosixapi.user.dto.UserInfoDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MyPageViewController {

    @GetMapping("/mypage")
    public String myPage(Model model){
        return "myPage";
    }

}
