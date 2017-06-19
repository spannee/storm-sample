package com.axisrooms.storm.rabbitMQ;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by Santhosh on 6/13/17.
 */
public interface QueueDeclaration extends Serializable {

    /**
     * Declare the queue, and any exchanges and bindings that it needs.  Called
     * once to determine the queue to consume from.
     * @param channel
     * @return
     * @throws IOException
     */
    AMQP.Queue.DeclareOk declare(Channel channel) throws IOException;

    /**
     * Indicate whether this queue is safe for parallel consumers.
     * @return
     */
    boolean isParallelConsumable();
}
