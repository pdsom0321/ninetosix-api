package com.gsc.ninetosixapi.login;


import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Api(tags = {"Login View API"})
public class LoginViewController {

    @RequestMapping(value="/login")
    public String login() {
        return "login";
    }

}
