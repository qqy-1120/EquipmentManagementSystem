package com.example.equimanage.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 设备位置
 * @TableName location
 */
@TableName(value ="location")
@Data
public class Location implements Serializable {
    /**
     * 位置id
     */
    @TableId(value = "location_id", type = IdType.AUTO)
    private Integer location_id;

    /**
     * 位置
     */
    @TableField(value = "location")
    private String location;

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