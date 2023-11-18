package com.example.equimanage.exception;

import com.example.equimanage.common.Constants;
import com.example.equimanage.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import cn.hutool.log.Log;

import java.io.IOException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RequestHandlingException.class)
    public Result requestHandlingExceptionHandler(RequestHandlingException re){
        return Result.failure(re.getCode(), re.getMessage());
    }

//    @ExceptionHandler(Exception.class)
//    public Result exceptionHandler(Exception e){
//        return Result.failure(Constants.CODE_500, e.getMessage());
//    }
}
