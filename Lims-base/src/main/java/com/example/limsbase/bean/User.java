package com.example.limsbase.bean;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_user")
public class User implements Serializable {
    private static final long serialVersionUID = -40356785423868312L;

    /**
    * 主键
    */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
    * 用户名
    */
    @TableField("user_name")
    private String userName;
    /**
    * 昵称
    */
    @TableField("nick_name")
    private String nickName;
    /**
    * 密码
    */
    @TableField("password")
    private String password;
    /**
    * 账号状态（0正常 1停用）
    */
    @TableField("status")
    private String status;
    /**
    * 邮箱
    */
    @TableField("email")
    private String email;
    /**
    * 手机号
    */
    @TableField("phonenumber")
    private String phonenumber;
    /**
    * 用户性别（0男，1女，2未知）
    */
    @TableField("sex")
    private String sex;
    /**
    * 头像
    */
    @TableField("avatar")
    private String avatar;
    /**
    * 用户类型（0管理员，1普通用户）
    */
    @TableField("user_type")
    private String userType;
    /**
    * 创建人的用户id
    */
    @TableField("create_by")
    private Long createBy;
    /**
    * 创建时间
    */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern =  "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
    * 更新人
    */
    @TableField("update_by")
    private Long updateBy;
    /**
    * 更新时间
    */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern =  "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    /**
    * 删除标志（0代表未删除，1代表已删除）
    */
    @TableField("del_flag")
    private Integer delFlag;

    @TableField(exist = false)
    private String captchaId;
    @TableField(exist = false)
    private String captchaCode;

}
