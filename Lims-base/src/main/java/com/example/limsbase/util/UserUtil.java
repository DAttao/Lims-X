package com.example.limsbase.util;

import com.example.limsbase.bean.login.LoginUser;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

public class UserUtil {

    /**
     * 获取当前登录用户的ID
     * @return 当前登录用户的ID
     */
    public static Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof LoginUser) {
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            return loginUser.getUser().getId(); // 假设LoginUser中有getUser()方法返回用户对象，并且用户对象有getId()方法
        }
        return Long.valueOf(0);
    }

    /**
     * 获取当前登录用户的信息
     * @return 当前登录用户的信息
     */
    public static LoginUser getCurrentLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof LoginUser) {
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            return loginUser;
        }
        return null;
    }




}
