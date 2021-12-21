package cn.edu.bjfu.activemq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * @author chaos
 * @date 2021-12-21 11:35
 */
public class JmsConsumer {
    public static final String ACTIVEMQ_URL = "tcp://82.156.4.243:61616";
    public static final String QUEUE_NAME = "queue01";

    public static void main(String[] args) throws JMSException {
        Connection connection = null;
        Session session = null;
        MessageConsumer consumer = null;
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
            Queue queue = session.createQueue(QUEUE_NAME);
            // 创建消息的消费者
            consumer = session.createConsumer(queue);
            // 同步阻塞方式receive()
            // receive方法在接收到消息之前（或超时之前）将一直阻塞
//            while (true) {
//                TextMessage message = (TextMessage) consumer.receive();
//                if (message != null) {
//                    System.out.println("消费者接收到消息--->" + message.getText());
//                } else {
//                    break;
//                }
//            }

            // 通过监听的方式来监听消息队列
            consumer.setMessageListener(message -> {
                if (message instanceof TextMessage) {
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        System.out.println("消费者监听并接收到消息--->" + textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            });
            System.out.println("阻塞，输入任意字符继续");
            System.in.read();
        } catch (JMSException | IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (consumer != null) {
                consumer.close();
            }
            if (session != null) {
                session.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        System.out.println("******消息处理完毕******");
    }
}
