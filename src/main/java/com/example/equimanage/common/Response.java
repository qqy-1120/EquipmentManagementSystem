package com.example.equimanage.common;


public class Response {
    private static String CODE_200 = "200"; // 请求成功
    private static String CODE_400 = "400"; // 参数错误
    private static String CODE_500 = "500"; // 内部服务器错误

    private static String CODE_600 = "600"; // 数据库错误

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

    // 参数错误
    public static class RequestParameterError extends Response{
        public RequestParameterError(){
            super(CODE_400, "Invalid parameters.");
        }
    }

    // 数据库错误
    public static class RecordCreateError extends Response {
        public RecordCreateError(){
            super(CODE_600, "Fail to save the requested item to database.");
        }
    }
    public static class RecordRemoveError extends Response{
        public RecordRemoveError(){
            super(CODE_600, "Fail to remove the requested item from database.");
        }
    }

    // 服务器错误
    public static class InternalServerError extends Response{
        public InternalServerError(int code){
            super(CODE_500, "Internal server error with code " + code +". Please report the issue. Thanks." );
        }
    }

    // fixme: 这一行要删掉
    public static class UsernameError extends Response{
        public UsernameError(){
            super(CODE_400, "User not found." );
        }
    }
    public static class UsernameOrPasswordWrongError extends Response{
        public UsernameOrPasswordWrongError(){
            super(CODE_400, "Wrong user name or password." );
        }
    }
    public static class UserAlreadyExistsError extends Response{
        public UserAlreadyExistsError(){
            super(CODE_400, "User already exists." );
        }
    }
    public static class UnsupportedFileFormatError extends Response{
        public UnsupportedFileFormatError(){
            super(CODE_400, "Unsupported file format (file ext. error)" );
        }
    }
    public static class UserNotLoggedInError extends Response {
        public UserNotLoggedInError() { super(CODE_400, "Please log in to your account."); }
    }
    public static class LoginFailedError extends Response {
        public LoginFailedError() { super(CODE_500, "Login failed, please try again."); }
    }
    public static class TokenExpiredError extends Response {
        public TokenExpiredError() { super(CODE_400, "Token has expired, please log in again."); }
    }

    public static class IllegalTokenError extends Response {
        public IllegalTokenError() { super(CODE_400, "Token is illegal, please log in again."); }
    }
    public static class LogoutFailedError extends Response {
        public LogoutFailedError() { super(CODE_500, "Logout failed, please try again."); }
    }

    public static class RedisConnectionError extends Response {
        public RedisConnectionError() { super(CODE_600, "Redis connection failed."); }
    }

}