# Queue发送消息与接收消息示例

maven

```xml
<dependency>
    <groupId>org.apache.activemq</groupId>
    <artifactId>activemq-all</artifactId>
    <version>5.16.0</version>
</dependency>
```



### sender

```java
public class Sender {

    public static void main(String[] args) throws Exception{

        //1，获取连接工厂 (String userName, String password, String brokerURL)
        String userName = ActiveMQConnectionFactory.DEFAULT_USER;
        String password = ActiveMQConnectionFactory.DEFAULT_PASSWORD;
        String brokerURL = "tcp://localhost:61616";// 启动console可以看见此端口

        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(userName,password,brokerURL);
        //2.获取一个向ActiveMQ的连接
        Connection connection = connectionFactory.createConnection();
        connection.start();//启动连接,Connection默认是关闭的
        //3.获取session (是否需要事务，自动确认消息)
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);

        //4.创建 destination ,或者queue都行
        //设置生产者
        Queue queue = session.createQueue("NB");
        MessageProducer producer = session.createProducer(queue);

        //5.向目的地写入消息
        //创建消息
        for(int i=0;i<20;i++){
            TextMessage textMessage = session.createTextMessage("nb --- "+i);
            producer.send(textMessage);
            System.err.println("send -- "+ textMessage.getText());
            Thread.sleep(500);
        }

        //
        //6。关闭连接
        connection.close();
        System.err.println("System exit......");
    }
}
```

### web控制台查看 

![1609595163242](D:\Z_lhy\STUDY\ActiveMq\doc\img\1609595163242.png)



### consumer

```java
public class Receiver {

    public static void main(String[] args) throws Exception{
        //1，获取连接工厂 (String userName, String password, String brokerURL)
        String userName = ActiveMQConnectionFactory.DEFAULT_USER;
        String password = ActiveMQConnectionFactory.DEFAULT_PASSWORD;
        String brokerURL = "tcp://localhost:61616";// 启动console可以看见此端口

        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(userName,password,brokerURL);
        //2.获取一个向ActiveMQ的连接
        Connection connection = connectionFactory.createConnection();
        connection.start();////启动连接 ,Connection默认是关闭的
        //3.获取session (是否需要事务，自动确认消息)
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);

        //4.创建 destination ,或者queue都行
        //设置生产者
        Queue queue = session.createQueue("NB");

        //5.通过Session对象创建MessageConsumer
        MessageConsumer consumer = session.createConsumer(queue);
        //接收消息 //阻塞
        while(true){

            TextMessage message = (TextMessage)consumer.receive();
            System.err.println("receive --- "+message.getText());
        }
    }
}
```

注意：接收方要调用connection的start方法才能接收到 

 运行接收者的代码，在ActiveMQ的web控制台观察消息数据 

![1609595374598](D:\Z_lhy\STUDY\ActiveMq\doc\img\1609595374598.png)

