package com.nb.msgfilter;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author lihaoyang
 * @date 2020/12/27
 */
public class Sender {

    public static void main(String[] args) throws Exception{


        String brokerURL = "tcp://localhost:61616";// 启动console可以看见此端口
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("system","123456",brokerURL);
        Connection connection = connectionFactory.createConnection();
        connection.start();//启动连接,Connection默认是关闭的
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("user");
        MessageProducer producer = session.createProducer(queue);


        Message message1 = session.createTextMessage("牛呗，潢川人士");
        message1.setIntProperty("age",30);
        message1.setStringProperty("name","牛呗");

        Message message2 = session.createTextMessage("西决，禹州人事");
        message2.setIntProperty("age",18);
        message2.setStringProperty("name","西决");

        Message message3 = session.createTextMessage("阿痞，周口人士");
        message3.setIntProperty("age",19);
        message3.setStringProperty("name","阿痞");

        Message message4 = session.createTextMessage("提督，濮阳人士");
        message4.setIntProperty("age",20);
        message4.setStringProperty("name","提督");

        producer.send(message1);
        producer.send(message2);
        producer.send(message3);
        producer.send(message4);
        connection.close();
        System.err.println("System exit......");
    }
}
