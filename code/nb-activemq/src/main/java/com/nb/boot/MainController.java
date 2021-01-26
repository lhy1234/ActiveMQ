package com.nb.boot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lihaoyang
 * @date 2021/1/25
 */
@RestController
public class MainController {

    @Autowired
    SenderService senderSrv;

    @RequestMapping("send")
    public String send() {
        senderSrv.send("springboot","hello~!");
        return "ok";
    }
}
