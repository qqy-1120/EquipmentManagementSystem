package com.example.equimanage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.equimanage.common.Response;
import com.example.equimanage.exception.RequestHandlingException;
import com.example.equimanage.mapper.UserMapper;
import com.example.equimanage.pojo.AuthUser;
import com.example.equimanage.pojo.User;
import com.example.equimanage.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserMapper userMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if(username == null || username.equals("")){
            throw new RequestHandlingException(new Response.RequestParameterError());
        }
        else {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("username", username);
            User res = userMapper.selectOne(queryWrapper);
            if(res == null) {
                throw new RequestHandlingException(new Response.UsernameError());
            }
            else {
                String groupName;
                if(res.getIs_manager()==1) groupName = "ADMIN";
                else groupName = "USER";
                return new AuthUser(res,groupName);
            }
        }
    }
}
