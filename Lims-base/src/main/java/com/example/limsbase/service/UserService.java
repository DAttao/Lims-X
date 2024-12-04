package com.example.limsbase.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.limsbase.bean.User;
import com.example.limsbase.util.Result;

import java.util.HashMap;

public interface UserService extends IService<User> {
    Result<HashMap<String, String>> login(User user);

    Result<String> logout();

}
