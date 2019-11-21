package com.crazy.tool.library.comment;


import com.crazy.tool.library.entity.MessageBody;
import com.crazy.tool.library.entity.UserConstant;
import com.crazy.tool.library.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @ClassName CrazyWebsocket
 * @Description 消息管理管道
 * @Author crazy006
 * @Date 2019/01/05 16:03
 **/
@ServerEndpoint("/ws/{userId}") //WebSocket客户端建立连接的地址
@Component
@Slf4j
public class CrazyWebsocket {
    /**
     * 注入redis
     */
    private static RedisTemplate redisTemplate;;
    /**
     * 在线用户数
     */
    private static int onlineCount = 0;

    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static CopyOnWriteArraySet<CrazyWebsocket> webSocketSet = new CopyOnWriteArraySet<CrazyWebsocket>();

    /*
     *与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    private String userId;

    @Autowired
    private void autowiredTokenRedisTemplate(RedisTemplate redisTemplate) {
        CrazyWebsocket.redisTemplate = redisTemplate;
    }

    /**
     * 连接建立成功调用的方法
     **/
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        try {
            String redisUserId = (String) redisTemplate.opsForValue().get(userId + UserConstant.USER_KEY);
            if (!StringUtils.isEmpty(redisUserId) && redisUserId.equals(userId)) {
                this.session = session;
                this.userId = userId;
                webSocketSet.add(this);     //加入set中
                addOnlineCount();           //在线数加1
                sendInfo(userId + ":online");
                log.info("有新连接加入！当前在线人数为:" + getOnlineCount());
            } else {
                session.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("IO异常");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        log.info("有一连接关闭！当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息 (json 格式)
     **/
    @OnMessage
    public void onMessage(String message, Session session) {
        MessageBody msg = (MessageBody) JsonUtil.fromJson(message, MessageBody.class);
        try {
            //发消息
            if (!StringUtils.isEmpty(msg.getTo())) {
                boolean flag = false;
                List<String> list = Arrays.asList(msg.getTo().split(","));
                for (CrazyWebsocket item : webSocketSet) {
                    if (list.contains(item.userId)) {
                        log.info("用户:" + msg.getTo() + " 接收到消息：" + message);
                        item.sendMessage(message);
                        flag = true;
                        //break;
                    }
                }
                if (!flag) {
                    msg.setCode("404");
                    msg.setRemark("接收端用户离线");
                    this.sendMessage(JsonUtil.toJson(msg));
                } else {
                    msg.setCode("200");
                    msg.setRemark("推送成功！！！");
                    this.sendMessage(JsonUtil.toJson(msg));
                }
            } else {
                sendInfo(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
            log.info("IO异常");
        }
    }

    /**
     * 发生错误时调用
     **/
    @OnError
    public void onError(Session session, Throwable error) {
        log.info("发生错误");
        error.printStackTrace();
    }

    /**
     * 发送消息
     *
     * @param message （json格式）
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
//        this.session.getBasicRemote().sendText(message);
        this.session.getAsyncRemote().sendText(message);
    }


    /**
     * 群发自定义消息
     **/
    public static void sendInfo(String message) throws IOException {
        for (CrazyWebsocket item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                continue;
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        CrazyWebsocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        if (onlineCount > 0){
            CrazyWebsocket.onlineCount--;
        }
    }
}
