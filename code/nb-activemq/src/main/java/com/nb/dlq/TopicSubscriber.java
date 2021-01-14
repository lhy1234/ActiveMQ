package com.nb.dlq;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author lihaoyang
 * @date 2021/1/2
 */
public class TopicSubscriber {

    public static final String BROKER_URL = "tcp://127.0.0.1:61616";
    public static final String DESTINATION = "NB-timeout";
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("system","123456",BROKER_URL);
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createTopic(DESTINATION);
        MessageConsumer messageConsumer = session.createConsumer(destination);
        while (true){
            //接收消息 有返回值，是阻塞的
            Message message = messageConsumer.receive();
            //判断消息类型
            if(message instanceof TextMessage){
                String text = ((TextMessage) message).getText();
                System.err.println("sub------"+text);
            }
        }

    }

}
