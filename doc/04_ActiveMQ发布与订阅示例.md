# ActiveMQ发布与订阅示例

 发布订阅和点对点的代码基本相同，只是修改一下目的地，在创建目的地的时候，将createQueue改为createTopic。 

topic的消息默认不持久化

消费者要比生产者先上线才能接收到消息

没有被消费的消息默认存在内存里，重启后就没有了。

![1610267350781](D:\Z_lhy\STUDY\ActiveMQ\doc\img\1610267350781.png)



## TopicPublisher 

```java
import org.apache.activemq.ActiveMQConnectionFactory;
import javax.jms.*;

public class TopicPublisher {

    public static final String BROKER_URL = "tcp://127.0.0.1:61616";
    //相当于一个数据库
    public static final String DESTINATION = "myTopic";
    public static void main(String[] args) {
        sendMessage();
    }
    public static void sendMessage(){
        //1 .创建一个连接工厂
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
        Connection connection = null;
        Session session = null;
        MessageProducer messageProducer = null;
        try {
            //2. 获取一个连接
            connection = connectionFactory.createConnection();
            //3. 创建一个Session 第一个参数：是否是事务消息 第二个参数：消息确认机制（自动确认还是手动确认）
            session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            //4. 有了session之后，就可以创建消息，目的地，生产者和消费者
            TextMessage message = session.createTextMessage("Hello ActiveMQ");
            //目的地
            Destination destination = session.createTopic(DESTINATION);
            //生产者
           for(int i=0;i<100;i++){
                TextMessage message = session.createTextMessage("Hello ActiveMQ");
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

```



## TopicSubscriber

```java
package com.nb.demo;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class TopicSubscriber {

    public static final String BROKER_URL = "tcp://127.0.0.1:61616";
    //相当于一个数据库（其实是一个队列）
    public static final String DESTINATION = "myTopic";
    public static void main(String[] args) {
        receiveMessage();
    }
    public static void receiveMessage(){
        //1 .创建一个连接工厂
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(BROKER_URL);
        Connection connection = null;
        Session session = null;
        MessageConsumer messageConsumer = null;
        try {
            //2. 获取一个连接
            connection = connectionFactory.createConnection();
            //接收消息，需要将连接启动一下，才可以接收到消息
            connection.start();
            //3. 创建一个Session 第一个参数：是否是事务消息 第二个参数：消息确认机制（自动确认还是手动确认）
            session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
            //4. 有了session之后，就可以创建消息，目的地，生产者和消费者
            //目的地
            Destination destination = session.createTopic(DESTINATION);
            //消费者
            messageConsumer = session.createConsumer(destination);
            //循环接收消息
            while (true){
                //接收消息 有返回值，是阻塞的
                Message message = messageConsumer.receive();
                //判断消息类型
                if(message instanceof TextMessage){
                    String text = ((TextMessage) message).getText();
                    System.err.println("sub------"+text);
                }
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }finally{
            try {
                if(messageConsumer != null){
                    messageConsumer.close();
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

```

消息订阅者先运行，然后再运行消息发布者 

管控台topics栏目

![1609596246554](D:\Z_lhy\STUDY\ActiveMq\doc\img\1609596246554.png)

