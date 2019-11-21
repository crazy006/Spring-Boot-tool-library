package com.crazy.tool.library.controller;

import com.crazy.tool.library.entity.UserConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @ClassName MessageController
 * @Description 消息管理器
 * @Author crazy006
 * @Date 2019/01/11 16:54
 **/

@Controller
@RequestMapping(value = "/im")
public class MessageController {
    @Value("${server.port}")
    private int port;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 首页
     * @param session
     * @param model
     * @return
     */
    @GetMapping(value = "/index")
    public String index(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("loginUser");
        model.addAttribute("userId", userId);
        return "im/index";
    }

    /**
     * 进入聊天室
     * @param userId
     * @param model
     * @return
     */
    @GetMapping(value = "/chat")
    public String chat(@RequestParam String userId, Model model){
        model.addAttribute("userId", userId);
        model.addAttribute("port", port);
        return "im/chat";
    }
    /**
     * 登陆页面
     * @return
     */
    @GetMapping(value = "/login")
    public String login() {
        return "im/login";
    }
    /**
     * 登陆
     * @param userId
     * @param password
     * @param session
     * @return
     */
    @PostMapping(value = "/login")
    public String login(@RequestParam String userId,
                                 @RequestParam(defaultValue = "123456", required = true) String password,
                                 HttpSession session) {
        if (!password.equals("123456")) {
            session.setAttribute("errorMsg", "密码错误");
        } else {
            //这里可以写token的安全过滤业务
            redisTemplate.opsForValue().set(userId + UserConstant.USER_KEY, userId);
            session.setAttribute("loginUser", userId);
        }
        return "redirect:/im/index";
    }
}
