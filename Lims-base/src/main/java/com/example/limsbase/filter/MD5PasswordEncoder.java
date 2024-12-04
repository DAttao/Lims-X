package com.example.limsbase.filter;

import com.example.limsbase.util.Md5Utils;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MD5PasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence rawPassword) {
        return Md5Utils.md5Encode(rawPassword.toString(), null);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return Md5Utils.md5Encode(rawPassword.toString(), null).equals(encodedPassword);
    }
}
