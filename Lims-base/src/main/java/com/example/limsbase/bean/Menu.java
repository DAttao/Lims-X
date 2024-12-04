package com.example.limsbase.bean;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 菜单表(Menu)实体类
 *
 * @author makejava
 * @since 2021-11-24 15:30:08
 */
@TableName(value="sys_menu")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Menu implements Serializable {
    private static final long serialVersionUID = -54979041104113736L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
    * 菜单名
    */
    @TableField("menu_name")
    private String menuName;
    /**
    * 路由地址
    */
    @TableField("path")
    private String path;
    /**
    * 组件路径
    */
    @TableField("component")
    private String component;
    /**
    * 菜单状态（0显示 1隐藏）
    */
    @TableField("visible")
    private String visible;
    /**
    * 菜单状态（0正常 1停用）
    */
    @TableField("status")
    private String status;
    /**
    * 权限标识
    */
    @TableField("perms")
    private String perms;
    /**
    * 菜单图标
    */
    @TableField("icon")
    private String icon;
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
    /**
    * 备注
    */
    @TableField("remark")
    private String remark;
}
