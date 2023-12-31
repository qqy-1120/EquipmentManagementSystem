package com.example.equimanage.service;

import com.example.equimanage.pojo.DTO.UserDTO;
import com.example.equimanage.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
* @author qqy
* @description 针对表【user】的数据库操作Service
* @createDate 2023-10-14 18:10:47
*/
public interface UserService extends IService<User> {

    Map<String,String> login(UserDTO userDTO);

    User register(UserDTO userDTO);

    String logout();
}
