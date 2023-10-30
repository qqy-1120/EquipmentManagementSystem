package com.example.equimanage.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.equimanage.common.Result;
import com.example.equimanage.common.Constants;
import com.example.equimanage.pojo.DTO.UserDTO;
import com.example.equimanage.pojo.User;
import com.example.equimanage.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO) {
        if(!isValid(userDTO)) {
            return Result.failure(Constants.CODE_400, "参数错误");
        } else {
            User res = userService.login(userDTO);
            return Result.success(res);
        }
    }

    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO) {
        if(!isValid(userDTO)) {
            return Result.failure(Constants.CODE_400, "参数错误");
        } else {
            return Result.success(userService.register(userDTO));
        }


    }

    @GetMapping("/user/list")
    public Result findAll() {
        return Result.success(userService.list());
    }
    private Boolean isValid(UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if(StringUtils.isBlank(username)||StringUtils.isBlank(password)){
            return false;
        } else return true;
    }
}
