package com.nb.listener;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author lihaoyang
 * @date 2020/12/27
 */
public class Sender {

    public static void main(String[] args) throws Exception{

        String brokerURL = "tcp://localhost:61616";
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("system","123456",brokerURL);
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("NB-Async");
        MessageProducer producer = session.createProducer(queue);
        for(int i=0;i<20;i++){
            TextMessage textMessage = session.createTextMessage("nb --- "+i);
            producer.send(textMessage);
            System.err.println("send -- "+ textMessage.getText());
            Thread.sleep(500);
        }

        //
        //6。关闭连接
        connection.close();
        System.err.println("System exit......");
    }
}
