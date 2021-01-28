package com.nb.async_producer;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author lihaoyang
 * @date 2020/12/27
 */
public class Sender {

    public static void main(String[] args) throws Exception{


        //发送消息异步配置方式一
        String brokerURL = "tcp://localhost:61616?jms.useAsyncSend=true";// 启动console可以看见此端口
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("system","123456",brokerURL);
        //发送消息异步配置方式2
        //connectionFactory.setUseAsyncSend(true);
        Connection connection = connectionFactory.createConnection();
        //发送消息异步配置方式3
        //((ActiveMQConnection)connection).setUseAsyncSend(true);
        connection.start();//启动连接

        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("user");
        MessageProducer producer = session.createProducer(queue);
        //producer.setDeliveryDelay(DeliveryMode.NON_PERSISTENT);


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
