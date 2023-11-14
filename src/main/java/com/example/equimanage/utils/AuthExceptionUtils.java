package com.example.equimanage.utils;

import com.example.equimanage.common.Constants;
import com.example.equimanage.common.Response;
import com.example.equimanage.common.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;

public class AuthExceptionUtils {
    public static Result getErrMsgByExceptionType(AuthenticationException e) {
        if (e instanceof CredentialsExpiredException || e instanceof BadCredentialsException) {
            return Result.failure(new Response.UsernameOrPasswordWrongError());
        } else if(e instanceof InsufficientAuthenticationException) {
            return Result.failure(new Response.UserNotLoggedInError());
        } else if(e instanceof AuthenticationServiceException) {
            return Result.failure(new Response.LoginFailedError());
        }
        return Result.failure(Constants.CODE_500, e.getMessage());
    }

    public static Result getErrMsgByExceptionType(AccessDeniedException e) {
        if (e instanceof AuthorizationServiceException)
            return Result.failure(Constants.CODE_500, "Authentication service exception, please try again.");
        else if (e instanceof AccessDeniedException)
            return Result.failure(new Response.UserNotLoggedInError());
        return Result.failure(Constants.CODE_500, e.getMessage());
    }
}
