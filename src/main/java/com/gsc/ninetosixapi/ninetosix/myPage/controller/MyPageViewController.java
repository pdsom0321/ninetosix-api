package com.gsc.ninetosixapi.ninetosix.myPage.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyPageViewController {

    @GetMapping("/mypage")
    public String myPage(Model model){
        return "myPage";
    }

}
