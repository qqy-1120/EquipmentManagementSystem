package com.example.equimanage.common;

interface ResultStatus {
    String CODE_200 = "200"; // 请求成功
    String CODE_400 = "400"; // 参数错误
    String CODE_401 = "401"; // 权限不足
    String CODE_500 = "500"; // 内部服务器错误
}

public class ErrorReport {
    public static class RequestParameterError {
        public static String CODE = ResultStatus.CODE_400;
        public static String MESSAGE = "Invalid Parameters";
    }
}