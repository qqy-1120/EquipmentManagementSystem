package com.example.equimanage.pojo.DTO;

import lombok.Data;

/**
 * 接受前端登录请求的参数
 */

@Data
public class UserDTO {
    private String username;
    private String password;
    private Integer is_manager;
}
