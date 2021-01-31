## 整合SpringBoot

### 配置文件

#### POM

```xml
<!--ActiveMQ-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-activemq</artifactId>
        </dependency>
        <dependency>
            <groupId>org.messaginghub</groupId>
            <artifactId>pooled-jms</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-pool2</artifactId>
        </dependency>

```

#### yml

```yaml
# 应用名称
spring:
  application:
    name: nb-activemq
  activemq:
    broker-url: tcp://localhost:61616
    user: admin
    password: admin
    pool:
      enabled: true
      #连接池最大连接数
      max-connections: 5
      #空闲的连接过期时间，默认30秒
      idle-timeout: 0
    packages:
      trust-all: true
  #默认不开启topic
  jms:
    pub-sub-domain: true
```

#### Config类

```java
@Configuration
@EnableJms
public class ActiveMqConfig {

    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerTopic(ConnectionFactory activeMQConnectionFactory) {
        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
        bean.setPubSubDomain(true);
        bean.setConnectionFactory(activeMQConnectionFactory);
        return bean;
    }
    // queue模式的ListenerContainer
    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerQueue(ConnectionFactory activeMQConnectionFactory) {
        DefaultJmsListenerContainerFactory bean = new DefaultJmsListenerContainerFactory();
        bean.setConnectionFactory(activeMQConnectionFactory);
        return bean;
    }
}
```

#### 发

```java
package com.mashibing.arika;

import java.util.ArrayList;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MqProducerService {

	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;
	

	
	
	public void sendStringQueue(String destination, String msg) {
		System.out.println("send...");
		ActiveMQQueue queue = new ActiveMQQueue(destination);
		jmsMessagingTemplate.afterPropertiesSet();
		
		ConnectionFactory factory = jmsMessagingTemplate.getConnectionFactory();
		
		try {
			Connection connection = factory.createConnection();
			connection.start();
			
			Session session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
			Queue queue2 = session.createQueue(destination);
			
			MessageProducer producer = session.createProducer(queue2);
			
			TextMessage message = session.createTextMessage("hahaha");
			
			
			producer.send(message);
		} catch (JMSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		jmsMessagingTemplate.convertAndSend(queue, msg);
	}
	public void sendStringQueueList(String destination, String msg) {
		System.out.println("xxooq");
		ArrayList<String> list = new ArrayList<>();
		list.add("1");
		list.add("2");
		jmsMessagingTemplate.convertAndSend(new ActiveMQQueue(destination), list);
	}
}
```

#### 收

```java
@Service
public class Receiver {


    @JmsListener(destination = "springboot",containerFactory = "jmsListenerContainerTopic")
    public void rece(String msg) {

        System.out.println("收到消息：" + msg);
    }
}
```



## 一般做法

yml配置队列名称

```yaml
nbQueue:  nbQueue
```

配置队列Bean

```java
@Configuration
public class BeanConfig {

    @Value("${nbQueue}")
    private String nbQueue;

    @Bean("nbQueue")
    public ActiveMQQueue nbQueue(){
        return new ActiveMQQueue(nbQueue);
    }
}

```

sender

```java
    @Autowired
    private ActiveMQQueue nbQueue;

    public void send4(String msg){
        jmsMessagingTemplate.convertAndSend(nbQueue,msg);
    }
```

receiver

```java
@JmsListener(destination = "${nbQueue}",containerFactory = "jmsListenerContainerQueue")
    public void receive(TextMessage textMessage) throws Exception{
        System.err.println("receive收到消息：" +textMessage.getText());
    }
```

