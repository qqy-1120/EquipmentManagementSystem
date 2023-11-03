package com.example.equimanage.controller;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.equimanage.common.Response;
import com.example.equimanage.common.Result;
import com.example.equimanage.common.Constants;
import com.example.equimanage.exception.RequestHandlingException;
import com.example.equimanage.pojo.DTO.UserDTO;
import com.example.equimanage.pojo.User;
import com.example.equimanage.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api")
@RestController
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO) {
        if(!isValid(userDTO)) {
            throw new RequestHandlingException(new Response.RequestParameterError());
        }
        else {
            User res = userService.login(userDTO);
            if(res == null) {
                //normally, we will not reach this block
                throw new RequestHandlingException(new Response.InternalServerError(Constants.ErrorCode.LOGIN_CONTROLLER));
            }
            return Result.success(res);
        }
    }
    private Boolean isValid(UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        if(StringUtils.isBlank(username)||StringUtils.isBlank(password)){
            return false;
        } else return true;
    }


    @PostMapping("/register")
    public Result register(@RequestBody UserDTO userDTO) {
        if(!isValid(userDTO)) {
            throw new RequestHandlingException(new Response.RequestParameterError());
        } else {
            User res = userService.register(userDTO);
            if(res == null) {
                //normally, we will not reach this block
                throw new RequestHandlingException(new Response.InternalServerError(Constants.ErrorCode.REGISTER_CONTROLLER));
            }
            return Result.success(res);
        }


    }

    @GetMapping("/user/list")
    public Result findAll() {
        //fixme: 一定要鉴权！鉴权！鉴权！
        List res = userService.list();
        if(res == null) {
            //normally, we will not reach this block
            throw new RequestHandlingException(new Response.InternalServerError(Constants.ErrorCode.REGISTER_CONTROLLER));
        }
        return Result.success(res);
    }

}
