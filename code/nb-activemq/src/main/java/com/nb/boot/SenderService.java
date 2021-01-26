package com.nb.boot;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lihaoyang
 * @date 2021/1/25
 */
@Service
public class SenderService {

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Autowired
    private JmsTemplate jmsTemplate;

    public void send(String destination,String message){
        ConnectionFactory connectionFactory = jmsTemplate.getConnectionFactory();


        try {
            Connection connection = connectionFactory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        jmsTemplate.send(destination, new MessageCreator() {

            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage(message);
                textMessage.setStringProperty("name","nb");
                return textMessage;
            }
        });
    }


    public void send222(String destination,String message){

        List<String> list = new ArrayList<>();
        list.add("牛呗");
        list.add("西决");
        list.add("提督");

        jmsMessagingTemplate.convertAndSend(destination,list);
    }

    public void send3(String destination, String msg) {


        ArrayList<String> list = new ArrayList<>();

        list.add("malaoshi");
        list.add("lain");
        list.add("zhou");
        jmsMessagingTemplate.convertAndSend(new ActiveMQQueue(destination), list);
    }
}
