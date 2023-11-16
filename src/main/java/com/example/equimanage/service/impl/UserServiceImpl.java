package com.example.equimanage.service.impl;

import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.equimanage.common.Constants;
import com.example.equimanage.common.Response;
import com.example.equimanage.common.Result;
import com.example.equimanage.exception.RequestHandlingException;
import com.example.equimanage.pojo.AuthUser;
import com.example.equimanage.pojo.DTO.UserDTO;
import com.example.equimanage.pojo.User;
import com.example.equimanage.service.UserService;
import com.example.equimanage.mapper.UserMapper;
import com.example.equimanage.utils.JwtUtils;
import com.example.equimanage.utils.RedisUtils;
import jakarta.annotation.Resource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private AuthenticationManager authenticationManager;

    @Override
    public Map<String, String> login(UserDTO userDTO) {
        //fixme: should we handle exceptions during findUserByName???
//        User userWithPwd = userMapper.findUserByName(userDTO.getUsername());
//        if(userWithPwd == null) {
//            throw new RequestHandlingException(new Response.UsernameError());
//        }
//        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//        if(!bCryptPasswordEncoder.matches(userDTO.getPassword(),userWithPwd.getPassword())) {
//            throw new RequestHandlingException(new Response.UsernameOrPasswordWrongError());
//        } else {
//            userWithPwd.setPassword(null);
//            return userWithPwd;
//        }
        // 通过loadUserByUsername进行认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDTO.getUsername(),userDTO.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        // 认证失败
        if(authenticate==null){
            throw new RequestHandlingException(new Response.UsernameOrPasswordWrongError());
        }

        // 认证通过，生成token
        AuthUser loginUser = (AuthUser) authenticate.getPrincipal();
        String userId = loginUser.getUser_id().toString();

        // token的payload
        Map<String, String> payloadMap = new HashMap<>();
        payloadMap.put("user_id",userId);
        payloadMap.put("username", loginUser.getUsername());
        Collection<? extends GrantedAuthority> authorities = loginUser.getAuthorities();
        String group = "";
        for (GrantedAuthority authority : authorities){
            group = authority.getAuthority();
        }
        //放group进去
        payloadMap.put("groupname",group);
        payloadMap.put("token", JwtUtils.generateToken(payloadMap));

        boolean resultRedis = redisUtils.set(userId, loginUser);

        if(!resultRedis){
            throw new RequestHandlingException(new Response.RedisConnectionError());
        }
        return payloadMap;
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
            // register接口不对外开放，仅仅作为数据导入使用
            // 干脆都别注册为管理员了，系统设定管理员
            // one.setIs_manager(userDTO.getIs_manager());
            one.setIs_manager(0);
            //fixme: should we handle exceptions during saving??
            // 数据库save失败
            if(save(one)==false) {
                throw new RequestHandlingException(new Response.RecordCreateError());
            }
            one.setPassword(null);
        } else {
            throw new RequestHandlingException(new Response.UserAlreadyExistsError());
        }
        return one;
    }

    // private static final Log LOG = Log.get();
    @Override
    public String logout() {
        try {
            // 获取SecurityContextHolder中的当前用户
            UsernamePasswordAuthenticationToken authenticationToken =
                    (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            AuthUser loginUser = (AuthUser) authenticationToken.getPrincipal();
            // 删除redis中的缓存
            String key = loginUser.getUser_id().toString();
            redisUtils.del(key);
        } catch (Exception e) {
            return e.getMessage();
        }
        return "";
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




