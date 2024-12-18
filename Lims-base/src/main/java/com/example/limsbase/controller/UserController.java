package com.example.limsbase.controller;

import com.aliyuncs.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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

// 以下功能模块都需要加入权限字段


    @GetMapping("/list")
    @PreAuthorize("hasAuthority('systerm:user:list')")//权限字段
    public Result<?> listUser(@RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "0") int pageSize,
                              User user){
        return userService.selectList(page, pageSize, user) ;
    }


    @PostMapping("/add")
    @PreAuthorize("hasAuthority('systerm:user:insert')")//权限字段
    public Result<?> addUser(@RequestBody User user){
        return userService.addUser(user);
    }

    @PostMapping("/edit")
    @PreAuthorize("hasAuthority('systerm:user:edit')")//权限字段
    public Result<?> editUser(@RequestBody User user){
        return userService.editUser(user);
    }


    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('systerm:user:delete')")//权限字段
    public Result<?> deleteUser(@PathVariable Long id){
        ;

        return userService.logicDelete(id) ;
    }

}
