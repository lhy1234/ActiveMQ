package com.nb.type;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 事务
 */
public class MsgTypeSender {

    public static void main(String[] args) throws Exception{


        String brokerURL = "tcp://localhost:61616";// 启动console可以看见此端口
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("system","123456",brokerURL);
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);

        Queue queue = session.createQueue("NB-User");
        MessageProducer producer = session.createProducer(queue);

        User user = new User(1,"牛呗",18);
        //ObjectMessage message = session.createObjectMessage(user);
//        MapMessage mapMessage = session.createMapMessage();
//        mapMessage.setInt("age",19);
//        mapMessage.setString("name","牛总");

//        BytesMessage bytesMessage = session.createBytesMessage();
//        bytesMessage.writeBoolean(true);
//        bytesMessage.writeUTF("你好牛总");
//        bytesMessage.writeBytes("你好牛".getBytes());

        StreamMessage message = session.createStreamMessage();
        message.writeLong(1000L);
        message.writeString("牛总您好");

        producer.send(message);




        //6。关闭连接
        connection.close();
        System.err.println("System exit......");
    }
}
