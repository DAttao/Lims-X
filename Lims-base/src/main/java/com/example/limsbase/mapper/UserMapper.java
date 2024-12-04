package com.example.limsbase.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.limsbase.bean.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
