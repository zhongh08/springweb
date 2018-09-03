package com.dachen.springweb.rabbitmq.workqueues;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Worker {

    private final static String QUEUE_NAME = "queue_test2";

    public static void main(String[] args) {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("127.0.0.1");
        //factory.setUsername("guest");
        //factory.setPassword("guest");
        factory.setPort(5672);

        try {
            // 创建一个新的连接
            final Connection connection = factory.newConnection();
            // 创建一个通道
            final Channel channel = connection.createChannel();
            // 声明要关注的队列
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            System.out.println("Consumer Waiting Received messages");

            channel.basicQos(1);

            final Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");

                    System.out.println(" [x] Received '" + message + "'");
                    try {
                        doWork(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        System.out.println(" [x] Done");
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    }
                }
            };

            boolean autoAck = false; // acknowledgment is covered below
            channel.basicConsume(QUEUE_NAME, autoAck, consumer);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void doWork(String task) throws InterruptedException {
        for (char ch: task.toCharArray()) {
            if (ch == '.') {
                Thread.sleep(1000);
            }
        }
    }

}
