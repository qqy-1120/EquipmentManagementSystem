package com.example.equimanage.exception;

import com.example.equimanage.common.Response;
import lombok.Getter;

@Getter
public class RequestHandlingException extends RuntimeException{
    private String code;

    public RequestHandlingException(String code, String msg) {
        super(msg);
        this.code = code;
    }
    public RequestHandlingException(Response response) {
        super(response.getMessage());
        this.code = response.getCode();
    }
}
