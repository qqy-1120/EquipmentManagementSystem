package com.example.equimanage.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 接口统一返回包装类
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {
    private String code;
    private String message;
    private Object data;

    public static Result success() {
        return new Result(Constants.CODE_200, "", null);
    }

    public static Result success(Object data) {
        return new Result(Constants.CODE_200, "HTTP OK", data);
    }

    public static Result failure(String code, String message) {
        return new Result(code, message, null);
    }
    public static Result failure(Response response) {
        //fixme: security hole, should be fixed!
        //用户操作连续错3/5/10次之类的，就应该block他五分钟或者一天之类的，然后后台写安全日志
        return new Result(response.getCode(), response.getMessage(),null);
    }
}
