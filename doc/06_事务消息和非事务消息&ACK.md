# 事务消息和非事务消息

## 事务消息

 创建会话Session指定transacted=true ，acknowledgeMode=Session.SESSION_TRANSACTED

```java
Session session = connection.createSession(true,Session.SESSION_TRANSACTED);
```

## 非事务消息

 创建会话Session指定transacted=false 

```java
Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
```

 事务消息必须在**发送和接收**完消息后显式地调用session.commit(); 

 事务性消息，不管设置何种消息确认模式，都会自动被确认，确认后，消息会从消息队列移除，因为**消息队列中的消息只会被消费一次，不能当做缓存或者永久性存储。** 

## 案例演示 

### sender：

```java
Session session = connection.createSession(true,Session.AUTO_ACKNOWLEDGE);
```

发送消息后不session.commit(); ,

运行程序，查看ActiveMQ数据库，还是没有待处理的消息 

```java
for(int i=0;i<20;i++){
            TextMessage textMessage = session.createTextMessage("nb --- "+i);
            producer.send(textMessage);
            System.err.println("send -- "+ textMessage.getText());
            //session.commit();
            Thread.sleep(500);
        }

```



![1609680256610](D:\Z_lhy\STUDY\ActiveMq\doc\img\1609680256610.png)

发送消息后session.commit();

```java
for(int i=0;i<20;i++){
            TextMessage textMessage = session.createTextMessage("nb --- "+i);
            producer.send(textMessage);
            System.err.println("send -- "+ textMessage.getText());
            session.commit();
            Thread.sleep(500);
        }

```

![1609680367190](D:\Z_lhy\STUDY\ActiveMq\doc\img\1609680367190.png)

还可以调用session.rollback进行消息回滚

```java
for(int i=0;i<5;i++) {
            TextMessage textMessage = session.createTextMessage("我是牛呗-"+i);
            producer.send(textMessage);
            if(i % 2==0){
                System.err.println("send：" + textMessage.getText());
                session.commit();
            }else {
                session.rollback();
            }
        }
```



### receiver

 修改Receiver类中的消息为事务消息 

```java
Session session = connection.createSession(true,Session.AUTO_ACKNOWLEDGE);
```

消费消息后不进行session.commit(); 

```java
 //5.通过Session对象创建MessageConsumer
        MessageConsumer consumer = session.createConsumer(queue);
        //接收消息 //阻塞
        while(true){
            TextMessage message = (TextMessage)consumer.receive();
            System.err.println("receive --- "+message.getText());
            //事务消息，消费完需要提交，否则队列里还有
            //session.commit();
        }
```

消息队列的消息还在，不会被删除，只有commit之后MQ里的消息才会被删除。



# 消息可靠性机制

 消息的确认指的是**接收消息**的时候发生的工作，发送消息不存在确认; 

 消息只有在被确认之后，才认为已经被**成功消费**，然后消息才会从队列或主题中删除。 

在事务性会话中，当一个事务被提交的时候，确认自动发生。

**消息的成功消费通常包含三个阶段:**

![1609681992402](D:\Z_lhy\STUDY\ActiveMq\doc\img\1609681992402.png)

- 客户接收消息  
- 客户处理消息   
- 消息被确认 

## 消息确认的方式

**消息接收确认机制，一律以接收者为准，与发送者没有关系。**

**事务性会话中：**acknowledgeMode取值**Session.SESSION_TRANSACTED **, 事务提交并确认。 配合事务消息的； 

在**非事务性会话**中，消息何时被确认取决于创建会话时的应答模式（acknowledgeMode）。

该参数有以下3个可选值：

**1、Session.AUTO_ACKNOWLEDGE**

消费者成功从receive方法返回时，或者从MessageListener.onMessage方法成功返回时，会话自动确认客户收到的消息。

**2、Session.CLIENT_ACKNOWLEDGE**

客户通过消息的acknowledge方法确认消息。

```java
TextMessage message = (TextMessage)consumer.receive();
message.acknowledge();
```

注意:这个确认机制和事务消息有重叠,不管是哪种确认机制,只要是事务消息,那么一旦事务提交,都会进行确认,所以需要如果客户端确认,需要改为非事务消息。 

需要注意的是，在这种模式中，确认是在会话层上进行：确认一个被消费的消息将自动确认所有已被会话消费的消息。例如，如果一个消息消费者消费了10个消息，然后确认第5个消息，那么所有10个消息都被确认。

**3、Session. DUPS_OK_ACKNOWLEDGE(很少用)**

不是必须确认，是一种“懒散的”消息确认，消息可能会重复发送，在第二次重新传送消息时，消息头的JMSRedelivered会被置为true标识当前消息已经传送过一次，客户端需要进行消息的重复处理控制。

该选择只是会话迟钝的确认消息的提交。如果JMS Provider失败，那么可能会导致一些重复的消息。

 Session不必确保对传送消息的签收，这个模式可能会引起消息的重复，但是降低了Session的开销，所以只有客户端能容忍重复的消息，才可使用。 



针对点对点Queue，不管是开启事务：

```java
 //接收消息 //阻塞
        while(true){
            TextMessage message = (TextMessage)consumer.receive();
            System.err.println("receive ： "+message.getText());
            //事务消息，消费完需要提交，否则队列里还有
            TimeUnit.MINUTES.sleep(10);
            session.commit();
        }
```

还是不开启事务：

```java
while(true){
            TextMessage message = (TextMessage)consumer.receive();
            TimeUnit.MINUTES.sleep(10);
            message.acknowledge();
            System.err.println("receive ： "+message.getText());
        }
```

### 多个消费者消费同一个队列

只要有客户端调用了consumer.receive();方法，**在receive的窗口期**，这条消息会被该客户端“锁定”，即使别的客户端也连接了同一个producer，也调用了receive();方法，也是不能消费到这个消息的，除非第一个消费者下线，第二个消费者才能获取到这条消息。

