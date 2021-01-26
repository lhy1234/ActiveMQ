package com.nb.boot;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

/**
 * @author lihaoyang
 * @date 2021/1/25
 */
@Service
public class Receiver {


    @JmsListener(destination = "springboot",containerFactory = "jmsListenerContainerTopic")
    public void rece(String msg) {

        System.out.println("收到消息：" + msg);
    }
}
