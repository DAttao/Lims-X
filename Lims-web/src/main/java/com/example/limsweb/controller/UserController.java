package com.example.limsweb.controller;

import com.aliyuncs.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.limsbase.bean.User;
import com.example.limsbase.bean.UserRole;
import com.example.limsbase.mapper.UserMapper;
import com.example.limsbase.mapper.UserRoleMapper;
import com.example.limsbase.service.UserService;
import com.example.limsbase.util.Md5Utils;
import com.example.limsbase.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lims/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleMapper userRoleMapper;

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
