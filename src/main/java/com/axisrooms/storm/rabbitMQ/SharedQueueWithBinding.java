package com.axisrooms.storm.rabbitMQ;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Santhosh on 6/13/17.
 */
public class SharedQueueWithBinding implements QueueDeclaration {

    private static final long serialVersionUID = 2364833412534518859L;

    private final String queueName;
    private final String exchange;
    private final String routingKey;

    public SharedQueueWithBinding(String queueName, String exchange, String routingKey) {
        this.queueName = queueName;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    @Override
    public AMQP.Queue.DeclareOk declare(Channel channel) throws IOException {
        channel.exchangeDeclarePassive(exchange);

        Map<String, Object> priorityArgs = new HashMap<>();
        priorityArgs.put("x-max-priority", 255);

        final AMQP.Queue.DeclareOk queue = channel.queueDeclare(queueName,true,false,false,priorityArgs);

        channel.queueBind(queue.getQueue(), exchange, routingKey);

        return queue;
    }

    @Override
    public boolean isParallelConsumable() {
        return false;
    }
}
