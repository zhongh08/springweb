package com.dachen.springweb.rabbitmq;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Customer {

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
            // 声明要关注的队列
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            System.out.println("Consumer Waiting Received messages");

            Consumer consumer = new DefaultConsumer(channel) {

                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                    String message = new String(body, "UTF-8");
                    System.out.println("Consumer Received:" + message);

                }

            };

            // 自动回复队列应答
            channel.basicConsume(QUEUE_NAME,true,consumer);

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
