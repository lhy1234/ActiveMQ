package com.nb.listener;


import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author lihaoyang
 * @date 2020/12/27
 */
public class Receiver {

    public static void main(String[] args) throws Exception{
        String brokerURL = "tcp://localhost:61616";
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("system","123456",brokerURL);
        Connection connection = connectionFactory.createConnection();
        connection.start();////启动连接 ,Connection默认是关闭的
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("NB-Async");

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

    }
}
