package com.crazy.tool.library.entity;

import lombok.Data;

/**
 * @ClassName ResultDto
 * @Description 结果集对象
 * @Author crazy006
 * @Date 2019/3/6 14:43
 * @Copyright 258web.com **/
@Data
public class ResultDto<T> {
    /**
     * 状态码
     */
    private Integer code;
    /**
     * 提示信息
     */
    private String message;
    /**
     * 返回内容
     */
    private T data;

    public void  setResponse(ResultEnum resultEnum, Object...args) {
        this.message = String.format(resultEnum.getMessage(), args);
        this.code = resultEnum.getCode();
    }
}
