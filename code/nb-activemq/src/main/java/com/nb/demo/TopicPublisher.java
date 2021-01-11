package com.nb.demo;

import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;
import java.util.concurrent.TimeUnit;

public class TopicPublisher {

    public static final String BROKER_URL = "tcp://127.0.0.1:61616";
    //相当于一个数据库
    public static final String DESTINATION = "myTopic";
    public static void main(String[] args) {
        sendMessage();
    }
    public static void sendMessage(){
        //1 .创建一个连接工厂
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("system","123456",BROKER_URL);
        Connection connection = null;
        Session session = null;
        MessageProducer messageProducer = null;
        try {
            //2. 获取一个连接
            connection = connectionFactory.createConnection();
            //3. 创建一个Session 第一个参数：是否是事务消息 第二个参数：消息确认机制（自动确认还是手动确认）
            session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            //4. 有了session之后，就可以创建消息，目的地，生产者和消费者
            for(int i=0;i<10;i++){
                TextMessage message = session.createTextMessage("Hello ActiveMQ--"+i);
                //目的地
                Destination destination = session.createTopic(DESTINATION);
                //生产者
                messageProducer = session.createProducer(destination);
                //发消息 没有返回值，是非阻塞的
                messageProducer.send(message);
                System.err.println("pub------"+message.getText());
                TimeUnit.SECONDS.sleep(1);
            }

        } catch (JMSException | InterruptedException e) {
            e.printStackTrace();
        }finally{
            try {
                if(messageProducer != null){
                    messageProducer.close();
                }
                if(session != null){
                    session.close();
                }
                if(connection != null){
                    connection.close();
                }
            }catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
