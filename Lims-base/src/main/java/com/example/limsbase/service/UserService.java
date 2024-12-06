package com.example.limsbase.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.limsbase.bean.User;
import com.example.limsbase.util.Result;

import java.util.HashMap;

public interface UserService extends IService<User> {
    Result<HashMap<String, String>> login(User user);
    Result<?> addUser(User user);
    Result<?> editUser(User user);

    Result<?> selectList(int startIndex,int pageSize,User user);
    Result<String> logout();
    Result<?> logicDelete(Long id);

}
