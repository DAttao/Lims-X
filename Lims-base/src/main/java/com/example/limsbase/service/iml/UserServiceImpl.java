package com.example.limsbase.service.iml;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.limsbase.bean.User;
import com.example.limsbase.bean.login.LoginUser;
import com.example.limsbase.mapper.UserMapper;
import com.example.limsbase.service.UserService;
import com.example.limsbase.util.JwtUtil;
import com.example.limsbase.util.RedisCache;
import com.example.limsbase.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisCache redisCache;

    @Override
    public Result<HashMap<String, String>> login(User user) {
        // 验证验证码
        String storedCaptcha = redisCache.getCacheObject2("captcha:" + user.getCaptchaId());

        if (storedCaptcha == null || !storedCaptcha.equalsIgnoreCase(user.getCaptchaCode())) {
            return Result.error("验证码错误或已过期");
        }

        // 移除已验证的验证码
        redisCache.deleteObject("captcha:" + user.getCaptchaId());
        //AuthenticationManager authenticate进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        //如果认证没通过
        if(Objects.isNull(authenticate)){
            return Result.error("登录失败，认证未通过!");
        }
        //如果认证通过了，使用userId生成一个jwt，jwt存入ResponseResult返回
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();

        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);

        //authenticate存入redis
        redisCache.setCacheObject("loginUserId:"+userId,loginUser);
        //把token响应给前端
        HashMap<String,String> map = new HashMap<>();
        map.put("token",jwt);

        return Result.OK("登陆成功",map);
    }

    @Override
    public Result<String> logout() {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userid = loginUser.getUser().getId();
        redisCache.deleteObject("loginUserId:"+userid);
        return new Result<String>().success("退出成功");
    }
}
