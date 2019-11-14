package com.crazy.tool;

import com.crazy.tool.library.entity.Crazy;
import com.crazy.tool.library.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest
class ToolApplicationTests {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    void contextLoads() {
    }

    /**
     * redis 服务测试
     */
    @Test
    void testRedis() {
        //缓存String对象
        redisTemplate.opsForValue().set("crazy006-test", "hello everybody");
        //获取缓存
        String s = (String) redisTemplate.opsForValue().get("crazy006-test");
        log.info("crazy006-test ======= " + s);
        //缓存Crazy对象
        Crazy crazy = new Crazy();
        crazy.setName("crazy006");
        crazy.setAge("18");
        redisTemplate.opsForValue().set("crazy006-object", crazy);
        Crazy crazyCache = (Crazy) redisTemplate.opsForValue().get("crazy006-object");
        log.info("crazy006-object===========" + JsonUtil.toJson(crazyCache));
        //list处理
        List<Crazy> list = new ArrayList<>();
        Crazy crazy1 = new Crazy();
        crazy1.setName("crazy007");
        crazy1.setAge("20");
        list.add(crazy);
        list.add(crazy1);
        redisTemplate.opsForValue().set("crazy006-cache-list", list);
        List<Crazy> cacheList = (List<Crazy>) redisTemplate.opsForValue().get("crazy006-cache-list");
        log.info("crazy-cache-list============" + cacheList);
        //Long对象处理
        Long l = new Long(111111);
        redisTemplate.opsForValue().set("crazy-cache-long", l);
        //从get缓存的时候不能直接转换成Long对象，否则会报转换类型错误
        Integer cacheL = (Integer) redisTemplate.opsForValue().get("crazy-cache-long");
        log.info("crazy-cache-long==" + cacheL);
    }

}
