package com.example.equimanage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.equimanage.common.Constants;
import com.example.equimanage.common.Response;
import com.example.equimanage.exception.RequestHandlingException;
import com.example.equimanage.pojo.DTO.UserDTO;
import com.example.equimanage.pojo.User;
import com.example.equimanage.service.UserService;
import com.example.equimanage.mapper.UserMapper;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
* @author qqy
* @description 针对表【user】的数据库操作Service实现
* @createDate 2023-10-14 18:10:47
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private UserMapper userMapper;

    @Override
    public User login(UserDTO userDTO) {
        //fixme: should we handle exceptions during findUserByName???
        User userWithPwd = userMapper.findUserByName(userDTO.getUsername());
        if(userWithPwd == null) {
            throw new RequestHandlingException(new Response.UsernameError());
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if(!bCryptPasswordEncoder.matches(userDTO.getPassword(),userWithPwd.getPassword())) {
            throw new RequestHandlingException(new Response.PasswordWrongError());
        } else {
            userWithPwd.setPassword(null);
            return userWithPwd;
        }
    }

    @Override
    public User register(UserDTO userDTO) {
        User one = getUserInfo(userDTO);
        if(one == null) {
            one = new User();
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            one.setUsername(userDTO.getUsername());
            one.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
            //fixme: 是不是应该鉴权：不能人人注册为管理员吧？
            one.setIs_manager(userDTO.getIs_manager());
            //fixme: should we handle exceptions during saving??
            save(one);
            one.setPassword(null);
        } else {
            throw new RequestHandlingException(new Response.UserAlreadyExistsError());
        }
        return one;
    }

    /**
     * 根据username，判断user表中是否存在用户
     * @param userDTO
     * @return UserDTO
     */
    private User getUserInfo(UserDTO userDTO){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userDTO.getUsername());
        // 希望是唯一的，在注册时注意，username应该是唯一键
        User one;
        try {
            one = getOne(queryWrapper);
        } catch (Exception e) {
            throw new RequestHandlingException(new Response.InternalServerError(Constants.ErrorCode.GETUSERINFO_SERVICE));
        }
        return one;
    }
}




