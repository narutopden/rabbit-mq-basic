package com.rabbit.pub;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

/**
 * @author Blue_Sky 7/14/21
 */
public class Send {

    private final static String EXCHANGE_NAME = "exchange_fanout";

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
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
            String message = "new post from topden.tech ";
            channel.basicPublish(EXCHANGE_NAME,"", null,message.getBytes(StandardCharsets.UTF_8));
            System.out.println("[*] message successfuly send to qeue");


        }
    }
}
