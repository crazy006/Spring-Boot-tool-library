package com.crazy.tool.library.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName MessageBody
 * @Description 消息实体
 * @Author crazy006
 * @Date 2018/11/28 16:26
 **/
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageBody implements Serializable {
    private String to;
    private Long date;
    private String from;
    private String content;
    private String code;
    private String remark;
}
