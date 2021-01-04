package com.nb.ack;


import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.concurrent.TimeUnit;

/**
 * @author lihaoyang
 * @date 2020/12/27
 */
public class ACKReceiver {

    public static void main(String[] args) throws Exception{

        String brokerURL = "tcp://localhost:61616";// 启动console可以看见此端口
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("system","123456",brokerURL);
        Connection connection = connectionFactory.createConnection();
        connection.start();////启动连接 ,Connection默认是关闭的
        Session session = connection.createSession(false,Session.CLIENT_ACKNOWLEDGE);
        Queue queue = session.createQueue("NB-ACK");
        MessageConsumer consumer = session.createConsumer(queue);
        while(true){
            TextMessage message = (TextMessage)consumer.receive();
            TimeUnit.MINUTES.sleep(10);
            message.acknowledge();
            System.err.println("receive ： "+message.getText());
        }

    }
}
