package com.example.equimanage.filter;

import cn.hutool.log.Log;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.equimanage.common.Response;
import com.example.equimanage.exception.RequestHandlingException;
import com.example.equimanage.pojo.AuthUser;
import com.example.equimanage.utils.JwtUtils;
import com.example.equimanage.utils.RedisUtils;
import io.micrometer.common.util.StringUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Resource
    private RedisUtils redisUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws RequestHandlingException, ServletException, IOException {
        String token = request.getHeader("token");
        if(StringUtils.isBlank(token)) {
            // token不存在，放行
            filterChain.doFilter(request,response);
            return;
        }

        String userId = null;
        try {
            DecodedJWT tokenInfo = JwtUtils.verifyToken(token);
            userId = tokenInfo.getClaim("user_id").asString();
        } catch (Exception e) {
            if(e instanceof TokenExpiredException) {
                throw new RequestHandlingException(new Response.TokenExpiredError());
            } else {
                throw new RequestHandlingException(new Response.IllegalTokenError());
            }
        }

        // 从redis获取用户信息
        String redisKey = userId;
        AuthUser authUser = (AuthUser)redisUtils.get(redisKey);
        if(authUser==null){
            throw new RequestHandlingException(new Response.UserNotLoggedInError());
        }
        else {
            // 将用户信息存入SecurityContextHolder
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(authUser,null,authUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            filterChain.doFilter(request,response);
        }
    }
}
