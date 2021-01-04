package com.nb.transaction;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 事务
 */
public class TransactionSender {

    public static void main(String[] args) throws Exception{

        //1，获取连接工厂 (String userName, String password, String brokerURL)
        String brokerURL = "tcp://localhost:61616";// 启动console可以看见此端口
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("system","123456",brokerURL);
        //2.获取一个向ActiveMQ的连接
        Connection connection = connectionFactory.createConnection();
        connection.start();//启动连接,Connection默认是关闭的
        //3.获取session 参数一：是否需要事务，参数二：消息自动确认还是手动确认
        //事务性消息，不管设置何种消息确认模式，都会自动被确认，确认后，消息会从消息队列移除，
        // 因为消息队列中的消息只会被消费一次，不能当做缓存或者永久性存储。
        Session session = connection.createSession(true,Session.SESSION_TRANSACTED);

        //4.创建 destination ,或者queue都行
        //设置生产者
        Queue queue = session.createQueue("NB-Transaction");
        MessageProducer producer = session.createProducer(queue);

        //5.向目的地写入消息
        //创建消息
        for(int i=0;i<5;i++) {
            TextMessage textMessage = session.createTextMessage("我是牛呗-"+i);
            producer.send(textMessage);
//            if(i % 2==0){
                System.err.println("send：" + textMessage.getText());
                session.commit();
//            }else {
//                session.rollback();
//            }
        }


        //6。关闭连接
        connection.close();
        System.err.println("System exit......");
    }
}
