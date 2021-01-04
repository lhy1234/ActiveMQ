# ActiveMQ

 ActiveMQ是Java开发的， 实现了JMS规范； 官网： http://activemq.apache.org/ 

 ![img](D:\Z_lhy\STUDY\ActiveMq\doc\img\11111.png) 





## 消息中间件应用场景

异步、解耦

消息队列最主要的功能就是 : 解耦异步

比如用户注册: 如果同步执行,需要320ms才能响应用户

![1609057532210](D:\Z_lhy\STUDY\ActiveMq\doc\img\1609057532210.png)

有了MQ,  解耦异步:

用户收集注册信息提交到MQ ,MQ进行ACK确认(20ms), 响应用户.MQ通知消费者消费,用户信息入库,发邮件.

![1609057937244](D:\Z_lhy\STUDY\ActiveMq\doc\img\1609057937244.png)

## 常用消息队列比较

| 特性MQ           | ActiveMQ   | RabbitMQ   | RocketMQ         | Kafka            |
| ---------------- | ---------- | ---------- | ---------------- | ---------------- |
| 生产者消费者模式 | 支持       | 支持       | 支持             | 支持             |
| 发布订阅模式     | 支持       | 支持       | 支持             | 支持             |
| 请求回应模式     | 支持       | 支持       | 不支持           | 不支持           |
| Api完备性        | 高         | 高         | 高               | 高               |
| 多语言支持       | 支持       | 支持       | java             | 支持             |
| 单机吞吐量       | 万级       | 万级       | 万级             | 十万级           |
| 消息延迟         | 无         | 微秒级     | 毫秒级           | 毫秒级           |
| 可用性           | 高（主从） | 高（主从） | 非常高（分布式） | 非常高（分布式） |
| 消息丢失         | 低         | 低         | 理论上不会丢失   | 理论上不会丢失   |
| 文档的完备性     | 高         | 高         | 高               | 高               |
| 提供快速入门     | 有         | 有         | 有               | 有               |
| 社区活跃度       | 高         | 高         | 有               | 高               |
| 商业支持         | 无         | 无         | 商业云           | 商业云           |

## JMS中的一些角色

**Broker**：消息服务器，启动一个ActiveMQ实例就叫一个Broker。

**producer** ：生产者，消息生产者是由会话创建的一个对象，用于把消息发送到一个目的地。

**Consumer：**消费者，消息消费者是由会话创建的一个对象，它用于接收发送到目的地的消息

消息的消费可以采用以下两种方法之一：

- 同步消费。通过调用消费者的receive方法从目的地中显式提取消息。receive方法可以一直阻塞到消息到达。
- 异步消费。客户可以为消费者注册一个消息监听器，以定义在消息到达时所采取的动作。

##  JMS两种消息传送模式

### 点对点( Point-to-Point) 

基于点对点的消息模型， 

消息生产者生产消息发送到 queue 中，然后消息消费者从 queue 中取出并且消费消息。 

消息被消费以后，queue 中不再有存储，所以消息消费者不可能消费到已经被消费的消息。 

Queue 支持存在多个消费者，但是对一个消息而言，消息只能被一个消费者消费,其它 的则不能消费此消息了。 当消费者不存在时，消息会一直保存，直到有消费消费

<img src="D:\Z_lhy\STUDY\ActiveMq\doc\img\image-20200110192535698.png" style="zoom:50%;" />

###  发布/订阅(Publish/Subscribe) 

<img src="D:\Z_lhy\STUDY\ActiveMq\doc\img\image-20200110192613518.png" style="zoom:50%;" />

基于订阅/发布的消息模型

消息生产者（发布）将消息发布到 topic 中，同时有多个消息消费者（订阅）消费该消 息。 发布到 topic 的消息会被所有订阅者消费。 当生产者发布消息，不管是否有消费者。都不会保存消息 。一定要先有消息的消费者，后有消息的生产者。

### PTP 和 PUB/SUB 简单对比

| 1                 | Topic（Pub/ Sub发布 订阅消息）                               | Queue（Point-to-Point 点对点）                               |
| ----------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| 有无状态          | topic 数据默认不落地，是无状态的。                           | Queue 数据默认会在 mq 服 务器上以文件形式保存，比如 Active MQ 一 般 保 存 在 $AMQ_HOME\data\kahadb 下 面。也可以配置成 DB 存储。 |
| 完整性保障        | 并不保证 publisher 发布的每条数 据，Subscriber 都能接受到。  | Queue 保证每条数据都能 被 receiver 接收。消息不超时。        |
| 消息是否会丢失    | 一般来说 publisher 发布消息到某 一个 topic 时，只有正在监听该 topic 地址的 sub 能够接收到消息；如果没 有 sub 在监听，该 topic 就丢失了。 | Sender 发 送 消 息 到 目 标 Queue， receiver 可以异步接收这 个 Queue 上的消息。Queue 上的 消息如果暂时没有 receiver 来 取，也不会丢失。前提是消息不 超时。 |
| 消息发布接 收策略 | 一对多的消息发布接收策略，监 听同一个topic地址的多个sub都能收 到 publisher 发送的消息。Sub 接收完 通知 mq 服务器 | 一对一的消息发布接收策 略，一个 sender 发送的消息，只 能有一个 receiver 接收。 receiver 接收完后，通知 mq 服务器已接 收，mq 服务器对 queue 里的消 息采取删除或其他操作。 |
|                   |                                                              |                                                              |

### Queue

队列存储，常用与点对点消息模型

默认只能由唯一的一个消费者处理。一旦处理消息删除。

### Topic

主题存储，用于订阅/发布消息模型

主题中的消息，会发送给所有的消费者同时处理。只有在消息可以重复处 理的业务场景中可使用。

Queue/Topic都是 Destination 的子接口

### ConnectionFactory

连接工厂，jms中用它创建连接

连接工厂是客户用来创建连接的对象，例如ActiveMQ提供的ActiveMQConnectionFactory。

### Connection

JMS Connection封装了客户与JMS提供者之间的一个虚拟的连接。

### Destination

消息的目的地

目的地是客户用来指定它生产的消息的目标和它消费的消息的来源的对象。JMS1.0.2规范中定义了两种消息传递域：点对点（PTP）消息传递域和发布/订阅消息传递域。 

点对点消息传递域的特点如下：

- 每个消息只能有一个消费者。
- 消息的生产者和消费者之间没有时间上的相关性。无论消费者在生产者发送消息的时候是否处于运行状态，它都可以提取消息。

发布/订阅消息传递域的特点如下：

- 每个消息可以有多个消费者。

- 生产者和消费者之间有时间上的相关性。

- 订阅一个主题的消费者只能消费自它订阅之后发布的消息。JMS规范允许客户创建持久订阅，这在一定程度上放松了时间上的相关性要求 。持久订阅允许消费者消费它在未处于激活状态时发送的消息。 

  

  在点对点消息传递域中，目的地被成为队列（queue）；在发布/订阅消息传递域中，目的地被成为主题（topic）

### Session

JMS Session是生产和消费消息的一个单线程上下文。会话用于创建消息生产者（producer）、消息消费者（consumer）和消息（message）等。会话提供了一个事务性的上下文，在这个上下文中，一组发送和接收被组合到了一个原子操作中。



### 下载

 http://activemq.apache.org/ 

### 安装启动

解压后直接执行 windows（有jdk就行）： 

```
apache-activemq-5.16.0/bin/win64/activemq.bat
```

### web控制台

 http://localhost:8161/ 

 通过8161端口访问 ，默认用户名密码admin/admin

<img src="D:\Z_lhy\STUDY\ActiveMq\doc\img\1609054153983.png" alt="1609054153983" style="zoom:50%;" />



#### broker管控台Home首页：

<img src="D:\Z_lhy\STUDY\ActiveMq\doc\img\1609054588244.png" alt="1609054588244" style="zoom:50%;" />

#### Queue消息队列

点对点，只能被消费一次

<img src="D:\Z_lhy\STUDY\ActiveMq\doc\img\1609055088243.png" alt="1609055088243" style="zoom:50%;" />



#### Topic主题



<img src="D:\Z_lhy\STUDY\ActiveMq\doc\img\1609055368071.png" alt="1609055368071" style="zoom:50%;" />

#### Subscribers订阅者

<img src="D:\Z_lhy\STUDY\ActiveMq\doc\img\1609055599746.png" alt="1609055599746" style="zoom:50%;" />



Connection连接信息

服务器向客户端支持的连接协议

ws : websocket

openwire : 默认的连接方式,底层是tcp或nio

mqtt: 物联网协议

<img src="D:\Z_lhy\STUDY\ActiveMq\doc\img\1609055670911.png" alt="1609055670911" style="zoom:50%;" />

#### Network

组集群时候用

<img src="D:\Z_lhy\STUDY\ActiveMq\doc\img\1609056030088.png" alt="1609056030088" style="zoom:50%;" />



#### Send:Web版客户端

<img src="D:\Z_lhy\STUDY\ActiveMq\doc\img\1609056276165.png" alt="1609056276165" style="zoom:50%;" />

### 修改访问端口

 修改 ActiveMQ 配置文件:activemq/conf/jetty.xml 

```xml
<bean id="jettyPort" class="org.apache.activemq.web.WebConsolePort" init-method="start">
    <property name="host" value="127.0.0.1"/>
    <property name="port" value="8161"/>
</bean>
```



## 消息可靠性机制

- 

### 持久性

JMS 支持以下两种消息提交模式：

- PERSISTENT。指示JMS Provider持久保存消息，以保证消息不会因为JMS Provider的失败而丢失。
- NON_PERSISTENT。不要求JMS Provider持久保存消息。

### 优先级

可以使用消息优先级来指示JMS Provider首先提交紧急的消息。优先级分10个级别，从0（最低）到9（最高）。如果不指定优先级，默认级别是4。需要注意的是，JMS Provider并不一定保证按照优先级的顺序提交消息。

### 消息过期

可以设置消息在一定时间后过期，默认是永不过期。

### 临时目的地

可以通过会话上的createTemporaryQueue方法和createTemporaryTopic方法来创建临时目的地。它们的存在时间只限于创建它们的连接所保持的时间。只有创建该临时目的地的连接上的消息消费者才能够从临时目的地中提取消息。

### 持久订阅

首先消息生产者必须使用PERSISTENT提交消息。客户可以通过会话上的createDurableSubscriber方法来创建一个持久订阅，该方法的第一个参数必须是一个topic，第二个参数是订阅的名称。 JMS Provider会存储发布到持久订阅对应的topic上的消息。如果最初创建持久订阅的客户或者任何其它客户使用相同的连接工厂和连接的客户ID、相同的主题和相同的订阅名再次调用会话上的createDurableSubscriber方法，那么该持久订阅就会被激活。JMS Provider会象客户发送客户处于非激活状态时所发布的消息。 持久订阅在某个时刻只能有一个激活的订阅者。持久订阅在创建之后会一直保留，直到应用程序调用会话上的unsubscribe方法。

### 本地事务

在一个JMS客户端，可以使用本地事务来组合消息的发送和接收。JMS Session接口提供了commit和rollback方法。事务提交意味着生产的所有消息被发送，消费的所有消息被确认；事务回滚意味着生产的所有消息被销毁，消费的所有消息被恢复并重新提交，除非它们已经过期。 事务性的会话总是牵涉到事务处理中，commit或rollback方法一旦被调用，一个事务就结束了，而另一个事务被开始。关闭事务性会话将回滚其中的事务。 需要注意的是，如果使用请求/回复机制，即发送一个消息，同时希望在同一个事务中等待接收该消息的回复，那么程序将被挂起，因为知道事务提交，发送操作才会真正执行。 需要注意的还有一个，消息的生产和消费不能包含在同一个事务中。