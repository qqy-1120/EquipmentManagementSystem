package com.example.equimanage.utils;

import cn.hutool.log.Log;
import com.example.equimanage.common.Constants;
import com.example.equimanage.common.Response;
import com.example.equimanage.common.Result;
import com.example.equimanage.exception.RequestHandlingException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;

// 认证异常工具类
public class AuthExceptionUtils {

    private static final Log LOG = Log.get();
    public static Result getErrMsgByExceptionType(AuthenticationException e) {
        if (e instanceof CredentialsExpiredException || e instanceof BadCredentialsException) {
            return Result.failure(new Response.UsernameOrPasswordWrongError());
        } else if(e instanceof InsufficientAuthenticationException) {
            return Result.failure(new Response.UserNotLoggedInError());
        } else if(e instanceof AuthenticationServiceException) {
            return Result.failure(new Response.LoginFailedError());
        }
        return Result.failure(Constants.CODE_500, e.getMessage().toString());
    }

    public static Result getErrMsgByExceptionType(AccessDeniedException e) {
        if (e instanceof AuthorizationServiceException)
            return Result.failure(Constants.CODE_500, "Authentication service error, please try again.");
        else if (e instanceof AccessDeniedException)
            return Result.failure(new Response.AccessDeniedError());
        return Result.failure(Constants.CODE_500, e.getMessage());
    }
}
