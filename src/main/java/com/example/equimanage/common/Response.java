package com.example.equimanage.common;


public class Response {
    private static String CODE_200 = "200"; // 请求成功
    private static String CODE_400 = "400"; // 参数错误
    private static String CODE_401 = "401"; // 权限不足
    private static String CODE_500 = "500"; // 内部服务器错误
    private String code;
    private String message;
    public String getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
    Response(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public static class RequestParameterError extends Response{
        public RequestParameterError(){
            super(CODE_400, "Invalid parameters.");
        }
    }
    public static class RecordCreateError extends Response {
        public RecordCreateError(){
            super(CODE_400, "Fail to save the requested item to database.");
        }
    }
    public static class RecordRemoveError extends Response{
        public RecordRemoveError(){
            super(CODE_400, "Fail to remove the requested item from database.");
        }
    }
    public static class InternalServerError extends Response{
        public InternalServerError(int code){
            super(CODE_500, "Internal server error with code " + code +". Please report the issue. Thanks." );
        }
    }
    public static class UsernameError extends Response{
        public UsernameError(){
            super(CODE_401, "User not found." );
        }
    }
    public static class PasswordWrongError extends Response{
        public PasswordWrongError(){
            super(CODE_401, "Wrong password." );
        }
    }
    public static class UserAlreadyExistsError extends Response{
        public UserAlreadyExistsError(){
            super(CODE_401, "User already exists." );
        }
    }
    public static class UnsupportedFileFormatError extends Response{
        public UnsupportedFileFormatError(){
            super(CODE_401, "Unsupported file format (file ext. error)" );
        }
    }

}