package com.example.equimanage.common;

public interface Constants {
    public interface ErrorCode{
        int LOGIN_CONTROLLER = 1;
        int REGISTER_CONTROLLER = 2;
        int GETUSERINFO_SERVICE = 3;
    }
    String CODE_200 = "200"; // 请求成功
    String CODE_400 = "400"; // 参数错误
    String CODE_401 = "401"; // 权限不足
    String CODE_500 = "500"; // 内部服务器错误
}
