package com.example.limsbase.service.iml;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.core.util.IdUtil;

import com.example.limsbase.util.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Service
public class CaptchaService {

    @Autowired
    private RedisCache redisCache;

    // 生成验证码
    public HashMap<String, String> generateCaptcha() {
        // 生成验证码对象
        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(200, 100, 4, 20);

        // 获取验证码的唯一标识符
        String captchaId = IdUtil.simpleUUID();

        // 将验证码存入 Redis（有效期5分钟）
        redisCache.setCacheObject("captcha:" + captchaId, captcha.getCode(), 360, TimeUnit.MINUTES);

        // 返回验证码图片和唯一标识符给前端
        HashMap<String, String> captchaMap = new HashMap<>();
        captchaMap.put("captchaId", captchaId);
        captchaMap.put("captchaImage", captcha.getImageBase64());

        return captchaMap;
    }
}
