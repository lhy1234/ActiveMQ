package com.nb.delay;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ScheduledMessage;

import javax.jms.*;
import java.util.Date;

/**
 * @author lihaoyang
 * @date 2020/12/27
 */
public class Sender {

    public static void main(String[] args) throws Exception{


        String brokerURL = "tcp://localhost:61616";// 启动console可以看见此端口
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("system","123456",brokerURL);
        Connection connection = connectionFactory.createConnection();
        connection.start();//启动连接
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("user");
        MessageProducer producer = session.createProducer(queue);

        NotifyPlan notifyPlan = getNotifyPlan();

        MapMessage mapMessage = session.createMapMessage();
        mapMessage.setString("trainCampId",notifyPlan.getObjectId());
        //延迟10秒发送
//        message.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 10*1000);
        //带间隔的重复发送
        long delay= notifyPlan.getPlanTime().getTime()-System.currentTimeMillis();
        long delay2= 10 * 1000;
        long period = 2000;
        //必须int类型
        int repeat  = 0;
        mapMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY,delay);
        mapMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_PERIOD,period);
        mapMessage.setIntProperty(ScheduledMessage.AMQ_SCHEDULED_REPEAT,repeat);



        producer.send(mapMessage);

        connection.close();
        System.err.println("System exit......");
    }

    public static  NotifyPlan getNotifyPlan(){
        NotifyPlan notifyPlan = new NotifyPlan();
        notifyPlan.setId(29);
        notifyPlan.setNotifyPlanId("1e798864cbae409286111b67c5e771e5");
        notifyPlan.setBusinessType(0);
        notifyPlan.setObjectId("5254378a949e4c24bf388166a5bf3a13");
        notifyPlan.setTemplateId(5);
        Date sendTime = DateUtil.parseDateTime("2021-01-29 13:01:00").toJdkDate();
        notifyPlan.setPlanTime(sendTime);
        notifyPlan.setRemark("xxxooo");
        return notifyPlan;
    }

}
