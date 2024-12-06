package com.example.limsbase.service.iml;

import com.aliyuncs.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.limsbase.bean.User;
import com.example.limsbase.bean.UserRole;
import com.example.limsbase.bean.login.LoginUser;
import com.example.limsbase.mapper.UserMapper;
import com.example.limsbase.mapper.UserRoleMapper;
import com.example.limsbase.service.UserService;
import com.example.limsbase.util.JwtUtil;
import com.example.limsbase.util.Md5Utils;
import com.example.limsbase.util.RedisCache;
import com.example.limsbase.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.*;

import static com.example.limsbase.util.UserUtil.getCurrentUserId;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisCache redisCache;
    @Autowired
    private UserRoleMapper userRoleMapper;
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
    public Result<?> addUser(User user){
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            return Result.error("无法获取当前用户 ID");
        }
        user.setCreateBy(currentUserId);
        user.setUpdateBy(currentUserId);
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());
        try {
            // 在保存用户之前对密码进行MD5加密
            if (!StringUtils.isEmpty(user.getPassword())) {
                user.setPassword(Md5Utils.md5Encode(user.getPassword(), "UTF-8"));
            }
            super.save(user);
        } catch (Exception e) {
            return Result.error("保存 User 失败: " + e.getMessage());
        }
        //默认权限就是超级管理员，有能力可以实现权限的绑定等功能
        userRoleMapper.insert(new UserRole(user.getId().toString(),"1"));
        return Result.ok("添加成功");
    }
    @Override
    public  Result<?> editUser(User user){
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            return Result.error("无法获取当前用户 ID");
        }
        user.setUpdateBy(currentUserId);
        user.setUpdateTime(LocalDateTime.now());
        if (!StringUtils.isEmpty(user.getPassword())) {
            user.setPassword(Md5Utils.md5Encode(user.getPassword(), "UTF-8"));
        }
        try {
            int rows = userMapper.updateById(user);
            if (rows == 0) {
                return Result.error("更新失败，ID:"+user.getId()+"不存在");
            }
        } catch (Exception e) {
            return Result.error("更新失败: " + e.getMessage());
        }
        return Result.ok("修改成功");
    };

    @Override
    public Result<?> selectList(int page, int pageSize, User user) {
        int startIndex = (page-1)*pageSize;
        List<?> list = userMapper.selectList(startIndex, pageSize, user);
        int total = userMapper.selectListTotal(user);
        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        return Result.OK("查询成功",result);
    }
    @Override
    public Result<String> logout() {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long userid = loginUser.getUser().getId();
        redisCache.deleteObject("loginUserId:"+userid);
        return new Result<String>().success("退出成功");
    }
    @Override
    public Result<?> logicDelete(Long id) {
        Long currentUserId = getCurrentUserId();
        if (currentUserId == null) {
            return Result.error("无法获取当前用户 ID");
        }

        User user = new User();
        user.setId(id);
        user.setDelFlag(1);
        user.setUpdateBy(currentUserId);
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);
        return new Result<String>().success("退出成功");
    }
}
