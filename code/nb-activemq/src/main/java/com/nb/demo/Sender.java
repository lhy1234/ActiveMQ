package com.nb.demo;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author lihaoyang
 * @date 2020/12/27
 */
public class Sender {

    public static void main(String[] args) throws Exception{

        //1，获取连接工厂 (String userName, String password, String brokerURL)
//        String userName = ActiveMQConnectionFactory.DEFAULT_USER;
//        String password = ActiveMQConnectionFactory.DEFAULT_PASSWORD;
        String brokerURL = "tcp://localhost:61616";// 启动console可以看见此端口

        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("system","123456",brokerURL);
        //2.获取一个向ActiveMQ的连接
        Connection connection = connectionFactory.createConnection();
        connection.start();//启动连接,Connection默认是关闭的
        //3.获取session (是否需要事务，自动确认消息)
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);

        //4.创建 destination ,或者queue都行
        //设置生产者
        Queue queue = session.createQueue("NB222");
        MessageProducer producer = session.createProducer(queue);

        //5.向目的地写入消息
        //创建消息
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
