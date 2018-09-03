package com.dachen.springweb.rabbitmq.helloworld;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Producer {

    private final static String QUEUE_NAME = "queue_test";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setPort(5672);

        Connection connection = null;
        Channel channel = null;

        try {
            // 创建一个新的连接
            connection = factory.newConnection();
            // 创建一个通道
            channel = connection.createChannel();
            // 声明一个队列
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            // 发送消息到队列中
            String message = "{\"temperature\":100}";
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes("UTF-8"));
            System.out.println("Producer Send:" + message);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                channel.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}
