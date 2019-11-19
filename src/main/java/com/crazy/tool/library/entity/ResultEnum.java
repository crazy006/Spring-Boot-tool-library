package com.crazy.tool.library.entity;

import lombok.Getter;

/**
 * Created by crazy006
 * 2018-10-23 22:53
 */
@Getter
public enum ResultEnum {

    ALL_USER_OFFLINE(404, "所有接收端%s用户离线"),
    PART_USER_OFFLINE(403, "部分接收端%s用户离线"),
    PUSH_SUCCESS(200, "发送成功！！"),
    SUCCESS(200, "成功！！"),
    ERROR(-1, "未知错误"),
    ERROR_TOKEN(203, "无效的Token！！"),
    ;

    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
