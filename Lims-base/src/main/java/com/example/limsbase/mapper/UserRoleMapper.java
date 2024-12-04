package com.example.limsbase.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.limsbase.bean.UserRole;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {
}
