package com.example.limsbase.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.limsbase.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    List<User> selectList(@Param("startIndex") int startIndex,
    @Param("pageSize") int pageSize,@Param("user")User user);
    int selectListTotal(@Param("user")User user);

}
