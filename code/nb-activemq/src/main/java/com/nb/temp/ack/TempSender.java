package com.nb.temp.ack;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;

import javax.jms.*;

/**
 * 事务
 */
public class TempSender {

    public static void main(String[] args) throws Exception{


        String brokerURL = "tcp://localhost:61616";// 启动console可以看见此端口
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("system","123456",brokerURL);
        Connection connection = connectionFactory.createConnection();
        connection.start();//启动连接,Connection默认是关闭的
        final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Queue queue = new ActiveMQQueue("testQueue2");
        //使用session创建一个TemporaryQueue
        TemporaryQueue replyQueue = session.createTemporaryQueue();

//接收消息，并回复到指定的Queue中（即replyQueue）
        MessageConsumer comsumer = session.createConsumer(queue);
        comsumer.setMessageListener(m -> {
            try {
                System.err.println("Get Message: " + ((TextMessage)m).getText());
                MessageProducer producer = session.createProducer(m.getJMSReplyTo());
                producer.send(session.createTextMessage("ReplyMessage"));
            } catch (JMSException e) { }
        });



        //使用同一个Connection创建另一个Session，来读取replyQueue上的消息。
        Session session2 = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
        MessageConsumer replyComsumer = session2.createConsumer(replyQueue);
        replyComsumer.setMessageListener(m -> {
            try {
                System.err.println("Get reply: " + ((TextMessage)m).getText());
            } catch (JMSException e) { }
        });
        MessageProducer producer = session.createProducer(queue);
        TextMessage message = session.createTextMessage("SimpleMessage");
        message.setJMSReplyTo(replyQueue);
        producer.send(message);
    }


}
