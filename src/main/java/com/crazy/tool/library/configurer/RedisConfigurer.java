package com.crazy.tool.library.configurer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName RedisConfigurer
 * @Description redis bean 注册配置
 * @Author crazy006
 * @Date 2019/11/13 15:55
 * @Copyright 258web.com
 **/
@Configuration
public class RedisConfigurer {
    @Bean("stringRedisTemplate")
    public RedisTemplate<String, String> stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        Jackson2JsonRedisSerializer<String> j = new Jackson2JsonRedisSerializer<>(String.class);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(j);

        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(j);
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}
