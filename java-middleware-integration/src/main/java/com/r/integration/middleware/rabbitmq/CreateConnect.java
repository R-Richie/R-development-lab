package com.r.integration.middleware.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

public class CreateConnect {
    public static void main(String[] args) throws IOException, TimeoutException, URISyntaxException, NoSuchAlgorithmException, KeyManagementException {
        ConnectionFactory factory = generateConnectionFactory();
        Connection connection = factory.newConnection();
        System.out.println("factory1:"+connection.isOpen());
        Channel channel = connection.createChannel();
        channel.basicQos(1);
        connection.close();
        ConnectionFactory factory2 = generateConnectionFactory2();
        Connection connection2 = factory2.newConnection();
        System.out.println("factory2:"+connection2.isOpen());
        connection2.close();

    }

    public static ConnectionFactory generateConnectionFactory(){
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("admin");
        factory.setPassword("123456");
        factory.setVirtualHost("my_vhost");
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        return factory;
    }
    public static ConnectionFactory generateConnectionFactory2() throws URISyntaxException, NoSuchAlgorithmException, KeyManagementException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://admin:123456@127.0.0.1:5672/my_vhost");
        return factory;
    }
}
