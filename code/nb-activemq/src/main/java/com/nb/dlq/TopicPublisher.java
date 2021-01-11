package com.nb.dlq;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.util.concurrent.TimeUnit;

public class TopicPublisher {

    public static final String BROKER_URL = "tcp://127.0.0.1:61616";
    public static final String DESTINATION = "NB-timeout";
    public static void main(String[] args) throws Exception {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("system","123456",BROKER_URL);
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            //4. 有了session之后，就可以创建消息，目的地，生产者和消费者
            for(int i=0;i<10;i++){
                TextMessage message = session.createTextMessage("msg--"+i);
                Destination destination = session.createTopic(DESTINATION);
                MessageProducer producer = session.createProducer(destination);
                producer.setTimeToLive(5000);
                //发消息 没有返回值，是非阻塞的
                producer.send(message);
                System.err.println("pub------"+message.getText());

            }
    }
}
