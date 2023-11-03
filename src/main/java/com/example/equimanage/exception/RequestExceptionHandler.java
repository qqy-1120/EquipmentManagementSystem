package com.example.equimanage.exception;

import com.example.equimanage.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice(basePackages = "com.example.equimanage.controller")
public class RequestExceptionHandler {
    @ExceptionHandler(RequestHandlingException.class)
    @ResponseBody
    public Result handler(RequestHandlingException se){
        return Result.failure(se.getCode(), se.getMessage());
    }
}
