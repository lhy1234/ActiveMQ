package com.nb.boot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.TextMessage;

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

    @JmsListener(destination = "${nbQueue}",containerFactory = "jmsListenerContainerQueue")
    public void receive(TextMessage textMessage) throws Exception{
        System.err.println("receive收到消息：" +textMessage.getText());
    }
}
