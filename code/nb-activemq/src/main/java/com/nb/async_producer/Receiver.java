package com.nb.async_producer;


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
        Queue queue = session.createQueue("user?consumer.exclusive=true");
        MessageConsumer consumer = session.createConsumer(queue);
        //监听目的地
        consumer.setMessageListener(message -> {
            TextMessage textMessage = (TextMessage)message;
            try {
                System.err.println("textMessage="+textMessage.getText());
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });

        //监听死信队列
//        Queue dlq = session.createQueue("ActiveMQ.DLQ");
//        MessageConsumer dlqConsumer = session.createConsumer(dlq);
//        dlqConsumer.setMessageListener((message)->{
//            TextMessage textMessage = (TextMessage)message;
//            try {
//                System.err.println("死信队列消息："+textMessage.getText());
//            } catch (JMSException e) {
//                e.printStackTrace();
//            }
//        });
    }
}
