package com.example.equimanage.filter;

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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Resource
    private RedisUtils redisUtils;

    // 添加CustomExceptionFilter过滤器来处理filter抛出的异常
    @Resource
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

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
                RequestHandlingException re = new RequestHandlingException(new Response.TokenExpiredError());
                resolver.resolveException(request, response, null, re);
                return;
            } else {
                RequestHandlingException re = new RequestHandlingException(new Response.IllegalTokenError());
                resolver.resolveException(request, response, null, re);
                return;
            }
        }

        // 从redis获取用户信息
        String redisKey = "login:" + userId;
        AuthUser authUser = (AuthUser)redisUtils.get(redisKey);
        if(authUser==null){
            RequestHandlingException re = new RequestHandlingException(new Response.UserNotLoggedInError());
            resolver.resolveException(request, response, null, re);
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
