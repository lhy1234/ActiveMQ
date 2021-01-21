package com.nb.async;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author lihaoyang
 * @date 2020/12/27
 */
public class Sender {

    public static void main(String[] args) throws Exception{


        String brokerURL = "tcp://localhost:61616?jms.useAsyncSend=true";// 启动console可以看见此端口
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("system","123456",brokerURL);
        Connection connection = connectionFactory.createConnection();
        connection.start();//启动连接
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("user");
        MessageProducer producer = session.createProducer(queue);

        for(int i=0;i<100;i++){
            TextMessage textMessage = session.createTextMessage("nb --- "+i);
            producer.send(textMessage);
            System.err.println("send -- "+ textMessage.getText());
            Thread.sleep(1000);
        }

        connection.close();
        System.err.println("System exit......");
    }
}
