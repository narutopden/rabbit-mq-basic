package com.rabbit.direct;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * @author Blue_Sky 7/14/21
 */
public class Recv1 {

    private final static String EXCHANGE_NAME = "exchange_direct";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("39.104.78.202");
        factory.setUsername("admin");
        factory.setPassword("password");
        factory.setVirtualHost("/dev");
        factory.setPort(5672);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();


        // binding with exchange, fanout, and broadcast type
        channel.exchangeDeclare(EXCHANGE_NAME,BuiltinExchangeType.DIRECT);
        // getting the queue name
        String queueName = channel.queueDeclare().getQueue();
        // binding exchanger and queue
        channel.queueBind(queueName,EXCHANGE_NAME,"errorRoutingKey");
        channel.queueBind(queueName,EXCHANGE_NAME,"infoRoutingKey");
        channel.queueBind(queueName,EXCHANGE_NAME,"debugRoutingKey");

        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
               System.out.println("the body: "+new String(body,"utf-8"));

               //manuel confriming the and not multiple
                channel.basicAck(envelope.getDeliveryTag(), false);

            }
        };
        channel.basicConsume(queueName,false,consumer);

    }
}
