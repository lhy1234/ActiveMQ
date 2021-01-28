package com.nb.msg_duiji;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author lihaoyang
 * @date 2020/12/27
 */
public class Sender {

    public static void main(String[] args) throws Exception{


        //producer每发送一个消息，统计一下发送的字节数，当字节数达到ProducerWindowSize值时，需要等待broker的确认，才能继续发送。
        String brokerURL = "tcp://localhost:61616?jms.producerWindowSize=1";
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("system","123456",brokerURL);
        Connection connection = connectionFactory.createConnection();
        connection.start();//启动连接
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("duiji");
        MessageProducer producer = session.createProducer(queue);

        String msg = "禹州，简称“钧”，古称阳翟、钧州，别称夏都、钧都、药都，河南省辖县级市，许昌代管； [1]  河南省首批历史文化名城，中原城市群南缘的中心城市 [2]  ；是河南省人民政府批复确定的中原经济区西南部区域性副中心城市，全国重要的中医药集散和加工、陶瓷研发、先进制造业基地 [3]  。截至2018年，全市下辖4个街道、20个镇，2个乡 [4-5]  ；总面积1461平方公里 [3]  ，总人口130.38万人 [6]  ，其中市区常住人口47万人 [2]  。\n" +
                "禹州地处中国华中地区、河南省中部 [7-8]  ，北距省会郑州80公里，位于中原经济区核心区，毗邻郑州航空港经济综合实验区；是全国发展改革试点市、中国环境艺术陶瓷生产基地、全国新型城镇化综合试点市、国家城乡融合发展试验区、河南省首批文化改革发展试验区 [2]  。\n" +
                "禹州因大禹治水有功受封于此而得名。 [2]  是黄帝部落活动的中心区域之一 [9]  ，夏朝都城、韩国都城、秦汉颍川郡治、金元明清的州治所 [10]  ，钧瓷唯一产地、明清全国四大中药材集散地；境内有具茨山文化 [11-12]  、伏羲文化 [13]  、黄帝文化 [9]  、大禹文化 [14]  、钧瓷文化 [15]  、中医药文化 [16]  等古文化；拥有大量的历史文化遗存和深厚的历史底蕴 [2]  ，孕育出韩非、吕不韦、张良、吴道子、晁错、褚遂良、邯郸淳、郭嘉、司马徽等历史名人 [17]  。\n" +
                "2019年，全市生产总值完成833.2亿元，增长7.8%； [18]  荣获中国优秀旅游城市、国家卫生城市、国家园林城市 [19-20]  、中国生态魅力市、中国最具魅力宜居宜业宜游城市等称号。 [2]  2020年5月18日，入围人民日报社举办的全囯传播热度百强市（县级）";
        for(int i=0;i<1000;i++){
            TextMessage textMessage = session.createTextMessage(msg+i);
            producer.send(textMessage);
            System.err.println("send -- "+ textMessage.getText());

        }

        connection.close();
        System.err.println("System exit......");
    }
}
