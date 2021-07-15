package com.rabbit.workQ_fast;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

/**
 * @author Blue_Sky 7/14/21
 * Play with RabbitMQ's work queue fair strategy in practice
 */
public class Send {

    private final static String QUEUE_NAME = "work_mq_rr";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("39.104.78.202");
        factory.setUsername("admin");
        factory.setPassword("password");
        factory.setVirtualHost("/dev");
        factory.setPort(5672);

        //JDK7 syntax or automatically close connection and channel
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            // sending 10 message to  Recv1 an Recv2 and let the take one by one alternatively
            for (int i=1;i<=10;i++) {
                String message = "Hello World! Tsering topdent lala: "+i;
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
                System.out.println(" [x] Sent '" + message + "'");
            }
        }
    }
}
