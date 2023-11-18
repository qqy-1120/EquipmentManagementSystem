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
import java.util.Map;

@RequestMapping("/api")
@RestController
public class UserController {
    @Resource
    private UserService userService;

    /**
     * 登录
     * @param userDTO
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody UserDTO userDTO) {
        if(!isValid(userDTO)) {
            throw new RequestHandlingException(new Response.RequestParameterError());
        }
        else {
            Map<String,String> res = userService.login(userDTO);
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


    /**
     * 注册
     * @param userDTO
     * @return
     */
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


    /**
     * 登出
     * @return
     */
    @PostMapping("/logout")
    public Result logout() {
        String result = userService.logout();
        if(StringUtils.isEmpty(result))
            return Result.success();
        else return Result.failure(new Response.LogoutFailedError());
    }


    /**
     * 查看所有使用者
     * @return
     */
    @GetMapping("/users")
    // 限制权限
    // @PreAuthorize("hasAuthority('ADMIN')")
    public Result findAll() {
        //fixme: 一定要鉴权！鉴权！鉴权！
        // 普通用户和管理员都有查看当前使用者列表的需求
        // &已经实现密码错误5次就限制登录5min的功能
        List<User> res = userService.list();
        for (User user : res) {
            user.setPassword("");
        }
        if(res == null) {
            //normally, we will not reach this block
            throw new RequestHandlingException(new Response.InternalServerError(Constants.ErrorCode.REGISTER_CONTROLLER));
        }
        return Result.success(res);
    }

}
