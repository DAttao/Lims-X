package com.example.limsbase.controller;

import com.example.limsbase.bean.User;
import com.example.limsbase.service.UserService;
import com.example.limsbase.service.iml.CaptchaService;
import com.example.limsbase.util.RedisCache;
import com.example.limsbase.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("lims/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    CaptchaService captchaService;
    @Autowired
    RedisCache redisCache;

    @GetMapping("/captcha")
    public Result<HashMap<String, String>> getCaptcha() {
        HashMap<String, String> captchaMap = captchaService.generateCaptcha();
        return Result.OK(captchaMap);
    }
    @PostMapping("/login")
    public Result<HashMap<String, String>> login(@RequestBody User user){

        return  userService.login(user);
    }

    @PostMapping("/logout")
    public Result<String> logout(){

        return  userService.logout();
    }

}
