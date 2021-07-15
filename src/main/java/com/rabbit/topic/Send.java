package com.rabbit.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

/**
 * @author Blue_Sky 7/14/21
 */
public class Send {

    private final static String EXCHANGE_NAME = "exchange_topic";

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
            // config to direct mode
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

            String errorMsg = "this is error message log";
            String infoMsg = "this is error info log";
            String debugMsg = "this is error debug log";

            //publishing message to EXCHANGE
            channel.basicPublish(EXCHANGE_NAME,"order.log.error", null,errorMsg.getBytes(StandardCharsets.UTF_8));
            channel.basicPublish(EXCHANGE_NAME,"order.log.info", null,infoMsg.getBytes(StandardCharsets.UTF_8));
            channel.basicPublish(EXCHANGE_NAME,"product.log.debug", null,debugMsg.getBytes(StandardCharsets.UTF_8));

            System.out.println("[*] message successfully send to EXCHANGE POINT");


        }
    }
}
