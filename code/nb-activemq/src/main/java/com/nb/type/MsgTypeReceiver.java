package com.nb.type;


import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQObjectMessage;

import javax.jms.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author lihaoyang
 * @date 2020/12/27
 */
public class MsgTypeReceiver {

    public static void main(String[] args) throws Exception{

        String brokerURL = "tcp://localhost:61616";// 启动console可以看见此端口
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("system","123456",brokerURL);
        //添加受信任的包
        List<String> list = new ArrayList<>();
        list.add(User.class.getPackage().getName());
        list.add("java.lang");
        list.add("java.lang");
        connectionFactory.setTrustedPackages(list);
       // connectionFactory.setTrustAllPackages(true);

        Connection connection = connectionFactory.createConnection();
        connection.start();////启动连接 ,Connection默认是关闭的
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("NB-User");
        MessageConsumer consumer = session.createConsumer(queue);

        consumer.setMessageListener((message)->{
            try {
                if(message instanceof ObjectMessage){
                    User user = (User)((ObjectMessage)message).getObject();
                    System.err.println(user.toString());
                }
                if(message instanceof MapMessage){
                    MapMessage mapMessage = (MapMessage)message;
                    System.err.println(mapMessage.getString("name")+mapMessage.getInt("age"));
                }
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

                if(message instanceof StreamMessage){
                    StreamMessage streamMessage = (StreamMessage)message;
                    System.err.println(streamMessage.readLong());
                    System.err.println(streamMessage.readString());
                }

            } catch (JMSException e) {
                e.printStackTrace();
            }
        });

    }
}
