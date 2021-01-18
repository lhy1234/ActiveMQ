package com.nb.dlq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.concurrent.TimeUnit;

public class TopicPublisher {

    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("system","123456","tcp://127.0.0.1:61616");
        Connection connection = connectionFactory.createConnection();
        Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
        //4. 有了session之后，就可以创建消息，目的地，生产者和消费者
        for(int i=0;i<10;i++){
            TextMessage message = session.createTextMessage("msg--"+i);
            Destination destination = session.createTopic("user");
            MessageProducer producer = session.createProducer(destination);
            producer.send(message);
            System.err.println("pub------"+message.getText());

        }
    }
}
