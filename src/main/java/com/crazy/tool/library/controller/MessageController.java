package com.crazy.tool.library.controller;

import com.crazy.tool.library.entity.ResultDto;
import com.crazy.tool.library.entity.ResultEnum;
import com.crazy.tool.library.entity.UserConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName MessageController
 * @Description 消息管理器
 * @Author crazy006
 * @Date 2019/01/11 16:54
 **/

@RestController
@RequestMapping(value = "/websocket")
public class MessageController {
    @Autowired
    private RedisTemplate redisTemplate;
    @PostMapping(value = "/login")
    public ResultDto webSocketLogin(@RequestParam String userId, @RequestParam (defaultValue = "888", required = true) String token){
        ResultDto result = new ResultDto();
        //这里可以写token的安全过滤业务
        redisTemplate.opsForValue().set(userId + UserConstant.USER_KEY, userId);
        result.setResponse(ResultEnum.SUCCESS);
        return result;
    }
}
