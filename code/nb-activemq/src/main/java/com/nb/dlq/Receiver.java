package com.nb.dlq;


import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author lihaoyang
 * @date 2020/12/27
 */
public class Receiver {

    public static void main(String[] args) throws Exception{

        String brokerURL = "tcp://localhost:61616";// 启动console可以看见此端口
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("system","123456",brokerURL);
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("NB-TTL");
        MessageConsumer consumer = session.createConsumer(queue);
        while(true){
            TextMessage message = (TextMessage)consumer.receive();
            System.err.println("receive --- "+message.getText());
        }
    }
}
