package com.example.limsbase.controller;

import com.aliyuncs.utils.StringUtils;
import com.example.limsbase.bean.User;
import com.example.limsbase.bean.UserRole;
import com.example.limsbase.mapper.UserRoleMapper;
import com.example.limsbase.service.UserService;
import com.example.limsbase.service.iml.CaptchaService;
import com.example.limsbase.util.Md5Utils;
import com.example.limsbase.util.RedisCache;
import com.example.limsbase.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

import static com.example.limsbase.util.UserUtil.getCurrentLoginUser;

@RestController
@RequestMapping("lims/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    CaptchaService captchaService;
    @Autowired
    RedisCache redisCache;
    @Autowired
    private UserRoleMapper userRoleMapper;

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


    @GetMapping("/getCurrentLoginUser")
    public Result<?> getLoginUser() {

        return Result.OK(getCurrentLoginUser());
    }
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('systerm:user:insert')")
    public Result<?> addUser(@RequestBody User user){
        // 在保存用户之前对密码进行MD5加密
        if (!StringUtils.isEmpty(user.getPassword())) {
            user.setPassword(Md5Utils.md5Encode(user.getPassword(), "UTF-8"));
        }

        userService.save(user);
        //默认权限就是超级管理员
        userRoleMapper.insert(new UserRole(user.getId().toString(),"1"));

        return Result.OK("添加成功") ;
    }


}
