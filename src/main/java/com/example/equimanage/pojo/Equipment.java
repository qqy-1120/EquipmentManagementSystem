package com.example.equimanage.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName equipment
 */
@TableName(value ="equipment")
@Data
public class Equipment implements Serializable {
    /**
     * 设备id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 设备名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 设备类别
     */
    @TableField(value = "class")
    private String category;

    /**
     * 购入时间
     */
    @TableField(value = "buy_time")
    private Date buy_time;

    /**
     * 国有资产编号
     */
    @TableField(value = "number")
    private String number;

    /**
     * 当前使用者
     */
    @TableField(value = "user")
    private String user;

    /**
     * 领用时间
     */
    @TableField(value = "receive_time")
    private Date receive_time;

    /**
     * 设备状态（正在使用/报废）
     */
    @TableField(value = "state")
    private Integer state;

    /**
     * 设备图片
     */
    @TableField(value = "photo_url")
    private String photo_url;

    /**
     * 设备位置
     */
    @TableField(value = "location")
    private String location;

    /**
     * 设备配置
     */
    @TableField(value = "configuration")
    private String configuration;

    /**
     * 
     */
    @TableField(value = "created_at")
    private Date created_at;

    /**
     * 
     */
    @TableField(value = "updated_at")
    private Date updated_at;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}