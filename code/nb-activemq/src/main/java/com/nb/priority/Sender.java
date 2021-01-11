package com.nb.priority;

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

        Queue queue = session.createQueue("NB-priority");
        MessageProducer producer = session.createProducer(queue);
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);


        for(int i=1;i<10;i++){
            TextMessage textMessage = session.createTextMessage("NB-priority-"+i);
            producer.send(queue,textMessage,DeliveryMode.PERSISTENT,i,10000);
            System.err.println("send -- "+ textMessage.getText());
        }

        connection.close();
        System.err.println("System exit......");
    }
}
