## ActiveMQ消息类型

### 1.TextMessage文本消息(常用)

携带一个java.lang.String作为有效数据(负载)的消息，可用于字符串类型的信息交换。

### 2.ObjectMessage对象消息

#### 发送端

```java
User user = new User(1,"牛呗",18);
ObjectMessage message = session.createObjectMessage(user);

//User ，实现序列化接口
public class User implements Serializable {
    private Integer id;
    private String name;
    private Integer age;

    //get set
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
```

#### 接收端

```java
consumer.setMessageListener((message)->{
            try {
                if(message instanceof ObjectMessage){
                    User user = (User)((ObjectMessage)message).getObject();
                    System.err.println(user.toString());
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });
```

报错：

```java
Forbidden class com.nb.type.User! This class is not trusted to be serialized as ObjectMessage payload.
```

**需要添加信任** 

 http://activemq.apache.org/objectmessage.html 

```java
//添加受信任的包
        List<String> list = new ArrayList<>();
        list.add(User.class.getPackage().getName());
        connectionFactory.setTrustedPackages(list);
//注意：Integer也报错，直接把java.lang包也加进去
 list.add("java.lang");
//信任所有的包
 connectionFactory.setTrustAllPackages(true);
```

打印：

User{id=1, name='牛呗', age=18}

### 3.MapMessage

#### 发送方

```java
 MapMessage mapMessage = session.createMapMessage();
        mapMessage.setInt("age",19);
        mapMessage.setString("name","牛总");
```

#### 接收方

```java
if(message instanceof MapMessage){
    MapMessage mapMessage = (MapMessage)message;
    System.err.println(mapMessage.getString("name")+mapMessage.getInt("age"));
}
//打印
牛总19
```

### 4.BytesMessage

 携带一组原始数据类型的字节流(字节数组)作为有效负载的消息。 

 注意:发送和接收的顺序必须一致。 

#### 发送方

```java
 BytesMessage bytesMessage = session.createBytesMessage();
bytesMessage.writeBoolean(true);
bytesMessage.writeUTF("你好牛总");
bytesMessage.writeBytes("你好牛".getBytes());
```

#### 接收方

注意:发送和接收的顺序必须一致。 

```java
if(message instanceof BytesMessage){
    BytesMessage bytesMessage = (BytesMessage)message;
    System.err.println(bytesMessage.readBoolean());
    System.err.println(bytesMessage.readUTF());

    byte[] b = new byte[1024];
    int len=-1;
    while ((len=bytesMessage.readBytes(b)) != -1){
        System.err.println(new String(b,0,len));
    }
}
//打印，注意顺序
true
你好牛总
你好牛
```

### 5.StreamMessage

携带一个原始数据类型流作为有效负载的消息，它保持了写入流时的数据类型，写入什么类型，则读取也需要是相同的类型。

注意:发送和接收的顺序必须一致。

#### 发送方

```java
 StreamMessage message = session.createStreamMessage();
message.writeLong(1000L);
message.writeString("牛总您好");
```



#### 接收方

```java
if(message instanceof StreamMessage){
    StreamMessage streamMessage = (StreamMessage)message;
    System.err.println(streamMessage.readLong());
    System.err.println(streamMessage.readString());
}
```



