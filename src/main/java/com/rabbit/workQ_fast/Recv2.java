package com.rabbit.workQ_fast;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author Blue_Sky 7/14/21
 * Play with RabbitMQ's work queue fair strategy in practice
 */
public class Recv2 {

    private final static String QUEUE_NAME = "work_mq_rr";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("39.104.78.202");
        factory.setUsername("admin");
        factory.setPassword("password");
        factory.setVirtualHost("/dev");
        factory.setPort(5672);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        /**
         * prefetchCount is the number of messages can get per/time
         * and wont wait for other consumer to finish,
         * it will consume as much as possible
         */
        int numberOfMessagesPerTime = 1;
        channel.basicQos(numberOfMessagesPerTime);

        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                System.out.println("consumer tag: "+consumerTag);
//                System.out.println("Envelope: "+envelope);
//                System.out.println("properties: "+properties);
                System.out.println("body: "+new String(body,"UTF-8"));
                channel.basicAck(envelope.getDeliveryTag(),false);

            }
        };
        channel.basicConsume(QUEUE_NAME,false,consumer);

//        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
//            String message = new String(delivery.getBody(), "UTF-8");
//            System.out.println(" [x] Received '" + message + "'");
//        };
//        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
    }
}
