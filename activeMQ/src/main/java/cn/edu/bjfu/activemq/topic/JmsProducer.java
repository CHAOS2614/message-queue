package cn.edu.bjfu.activemq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author chaos
 * @date 2021-12-21 12:04
 */
public class JmsProducer {

    public static final String ACTIVEMQ_URL = "tcp://82.156.4.243:61616";
    public static final String TOPIC_NAME = "topic01";

    public static void main(String[] args) throws JMSException {
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        try {
            // 创建连接工厂，按照给定的url地址，采用默认用户名和密码
            ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
            // 通过连接工厂获得connection并启动访问
            connection = activeMQConnectionFactory.createConnection();
            connection.start();
            // 创建回话session
            // 两个参数，事务 签收
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // 创建目的地（队列or主题）
            Topic topic = session.createTopic(TOPIC_NAME);
            // 创建消息的生产者
            producer = session.createProducer(topic);
            // 通过使用消息生产者发送消息到MQ队列中
            for (int i = 0; i < 3; i++) {
                // 创建消息
                TextMessage textMessage = session.createTextMessage("mes--->" + (i + 1));
                // 将消息通过生产者发送给MQ
                producer.send(textMessage);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (producer != null) {
                producer.close();
            }
            if (session != null) {
                session.close();
            }
            if (connection != null) {
                connection.close();
            }
        }


        System.out.println("******消息发布完毕******");
    }

}
