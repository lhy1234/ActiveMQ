package com.nb.delay;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ScheduledMessage;

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
        connection.start();//启动连接
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("user");
        MessageProducer producer = session.createProducer(queue);


        TextMessage message = session.createTextMessage("ooxx");
        //延迟10秒发送
//        message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 10*1000);
        //带间隔的重复发送
        long delay= 10*1000;
        long period = 2000;
        int repeat  = 5;
        message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY,delay);
        message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD,period);
        message.setIntProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT,repeat);



        producer.send(message);

        connection.close();
        System.err.println("System exit......");
    }
}
