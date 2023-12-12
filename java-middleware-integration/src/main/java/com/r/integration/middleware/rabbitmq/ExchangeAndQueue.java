package com.r.integration.middleware.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ExchangeAndQueue {
    private static void createExangeAndQueue() throws IOException, TimeoutException {
        // 创建一个持久化、非自动删除、绑定类型为direct的交换器，同时创建一个非持久化的、排他的、自动删除的队列
        ConnectionFactory factory = CreateConnect.generateConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        String exchangeName = "";
        channel.exchangeDeclare(exchangeName, "direct", true);
        String queueName = channel.queueDeclare().getQueue();
        String routingKey = "";
        channel.queueBind(queueName, exchangeName, routingKey);
    }

    private static void createExangeAndQueue2() throws IOException, TimeoutException {
        // 创建一个持久化的、非排他的、非自动删除的队列
        ConnectionFactory factory = CreateConnect.generateConnectionFactory();
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        String exchangeName = "";
        channel.exchangeDeclare(exchangeName, "direct", true);
        String queueName = "";
        channel.queueDeclare(queueName, true, false,false, null);
        String routingKey = "";
        channel.queueBind(queueName, exchangeName, routingKey);
    }
}
