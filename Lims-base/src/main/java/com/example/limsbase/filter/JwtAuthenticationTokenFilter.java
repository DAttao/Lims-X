package com.example.limsbase.filter;

import com.example.limsbase.bean.login.LoginUser;
import com.example.limsbase.exception.Sc401Exception;
import com.example.limsbase.util.JwtUtil;
import com.example.limsbase.util.RedisCache;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.EOFException;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisCache redisCache;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //获取token
        String token = request.getHeader("token");
        if (!StringUtils.hasText(token)) {
            //放行
            filterChain.doFilter(request, response);
            return;
        }
        //解析token
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Sc401Exception("token非法");
        }
        //从redis中获取用户信息
        String redisKey = "loginUserId:"+userid;
        LoginUser loginUser = redisCache.getCacheObject(redisKey, LoginUser.class);
        if(Objects.isNull(loginUser)){
            throw new Sc401Exception("用户未登录");
        }
        //存入SecurityContextHolder
        //TODO 获取权限信息封装到Authentication中
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser,null,loginUser.getAuthorities());//权限信息
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(request, response);
    }


//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        // 获取 token
//        String token = request.getHeader("token"); // Web 端使用 token 头
//        if (!StringUtils.hasText(token)) {
//            token = request.getHeader("staffToken"); // 小程序端使用 staffToken 头
//        }
//
//        if (!StringUtils.hasText(token)) {
//            // 没有 token，放行请求
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        // 解析 token
//        String userid;
//        String redisKeyPrefix;
//        Object userObject;
//        try {
//            Claims claims = JwtUtil.parseJWT(token);
//            userid = claims.getSubject();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new EOFException("Token 非法");
//        }
//
//        // 根据 token 来源设置 Redis 键的前缀和用户对象类型
//        if (request.getHeader("token") != null) {
//            redisKeyPrefix = "loginUserId:";
//            userObject = redisCache.getCacheObject(redisKeyPrefix + userid, LoginUser.class);
//        } else {
//            redisKeyPrefix = "loginStaffId:";
//            userObject = redisCache.getCacheObject(redisKeyPrefix + userid, ScStaff.class);
//        }
//
//        if (Objects.isNull(userObject)) {
//            throw new EOFException("用户未登录");
//        }
//
//        // 存入 SecurityContextHolder
//        UsernamePasswordAuthenticationToken authenticationToken;
//        if (userObject instanceof LoginUser) {
//            authenticationToken = new UsernamePasswordAuthenticationToken(userObject, null, ((LoginUser) userObject).getAuthorities()); // 添加权限信息
//        } else {
//            authenticationToken = new UsernamePasswordAuthenticationToken(userObject, null, null); // 添加权限信息
//        }
//        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//
//        // 放行请求
//        filterChain.doFilter(request, response);
//    }
}
