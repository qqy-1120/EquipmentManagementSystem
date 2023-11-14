package com.example.equimanage.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;

/**
 * 
 * @TableName user
 */
@NoArgsConstructor
@TableName(value ="user")
@Data
public class User implements Serializable {
    /**
     * ID
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer user_id;

    /**
     * username
     */
    @TableField(value = "username")
    private String username;

    /**
     * 
     */
    @TableField(value = "password")
    private String password;

    /**
     * 
     */
    @TableField(value = "is_manager")
    private Integer is_manager;

    @TableField(value = "created_at")
    private Date created_at;

    @TableField(value = "updated_at")
    private Date updated_at;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public User(User user) {
        BeanUtils.copyProperties(user,this);
    }
}