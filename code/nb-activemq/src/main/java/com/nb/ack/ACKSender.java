package com.nb.ack;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 事务
 */
public class ACKSender {

    public static void main(String[] args) throws Exception{


        String brokerURL = "tcp://localhost:61616";// 启动console可以看见此端口
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("system","123456",brokerURL);
        Connection connection = connectionFactory.createConnection();
        connection.start();//启动连接,Connection默认是关闭的
        Session session = connection.createSession(false,Session.CLIENT_ACKNOWLEDGE);

        Queue queue = session.createQueue("NB-ACK");
        MessageProducer producer = session.createProducer(queue);
        for(int i=0;i<5;i++) {
            TextMessage textMessage = session.createTextMessage("我是牛呗-"+i);
            producer.send(textMessage);
            System.err.println("send：" + textMessage.getText());

        }


        //6。关闭连接
        connection.close();
        System.err.println("System exit......");
    }
}
