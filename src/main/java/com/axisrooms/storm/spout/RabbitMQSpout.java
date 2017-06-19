package com.axisrooms.storm.spout;

import com.axisrooms.storm.rabbitMQ.Constants;
import com.axisrooms.storm.rabbitMQ.QueueDeclaration;
import com.rabbitmq.client.*;
import org.apache.log4j.Logger;
import org.apache.storm.spout.Scheme;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.utils.Utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Created by Santhosh on 6/13/17.
 */
public class RabbitMQSpout implements IRichSpout {

    private static final long serialVersionUID = 11258942292629263L;

    private static final Logger log = Logger.getLogger(RabbitMQSpout.class);

    private final String amqpHost;
    private final int amqpPort;
    private final String amqpUsername;
    private final String amqpPassword;
    private final String amqpVhost;

    //private final QueueDeclaration queueDeclaration;
    private final QueueDeclaration queueDeclaration;

    private final Scheme serialisationScheme;

    private transient Connection amqpConnection;
    private transient Channel amqpChannel;
    private transient QueueingConsumer amqpConsumer;
    private transient String amqpConsumerTag;

    private SpoutOutputCollector collector;

    private int prefetchCount;

    public RabbitMQSpout(String host, int port, String username, String password, String vhost, QueueDeclaration queueDeclaration, Scheme scheme) {
        this.amqpHost = host;
        this.amqpPort = port;
        this.amqpUsername = username;
        this.amqpPassword = password;
        this.amqpVhost = vhost;
        this.queueDeclaration = queueDeclaration;

        this.serialisationScheme = scheme;
    }

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        Long prefetchCount = (Long) conf.get(Constants.CONFIG_PREFETCH_COUNT);
        if (prefetchCount == null) {
            log.info("Using default prefetch-count");
            prefetchCount = Constants.DEFAULT_PREFETCH_COUNT;
        } else if (prefetchCount < 1) {
            throw new IllegalArgumentException(Constants.CONFIG_PREFETCH_COUNT + " must be at least 1");
        }
        this.prefetchCount = prefetchCount.intValue();

        try {
            this.collector = collector;

            setupAMQP();
        } catch (IOException e) {
            log.error("AMQP setup failed", e);
        } catch (TimeoutException e) {
            log.error("AMQP setup failed", e);
        }
    }

    @Override
    public void close() {
        try {
            if (amqpChannel != null) {
                if (amqpConsumerTag != null) {
                    amqpChannel.basicCancel(amqpConsumerTag);
                }

                amqpChannel.close();
            }
        } catch (IOException e) {
            log.warn("Error closing AMQP channel", e);
        } catch (TimeoutException e) {
            log.warn("Error closing AMQP channel", e);
        }

        try {
            if (amqpConnection != null) {
                amqpConnection.close();
            }
        } catch (IOException e) {
            log.warn("Error closing AMQP connection", e);
        }
    }

    @Override
    public void activate() {

    }

    @Override
    public void deactivate() {

    }

    @Override
    public void nextTuple() {
        if (amqpConsumer != null) {
            try {
                final QueueingConsumer.Delivery delivery = amqpConsumer.nextDelivery(Constants.WAIT_FOR_NEXT_MESSAGE);
                if (delivery == null) return;
                final int priority = delivery.getProperties().getPriority();
                final long deliveryTag = delivery.getEnvelope().getDeliveryTag();
                final byte[] message = delivery.getBody();
                ByteBuffer messageBuffer = ByteBuffer.wrap(message);
                log.info("The priority for this message is " + priority);
                System.out.println("The priority for this message is " + priority);
                collector.emit(serialisationScheme.deserialize(messageBuffer), deliveryTag);
                /*
                 * TODO what to do about malformed messages? Skip?
                 * Avoid infinite retry!
                 * Maybe we should output them on a separate stream.
                 */
            } catch (ShutdownSignalException e) {
                log.warn("AMQP connection dropped, will attempt to reconnect...");
                Utils.sleep(Constants.WAIT_AFTER_SHUTDOWN_SIGNAL);
                reconnect();
            } catch (InterruptedException e) {
                // interrupted while waiting for message, big deal
            }
        }
    }

    @Override
    public void ack(Object msgId) {

    }

    @Override
    public void fail(Object msgId) {
        if (msgId instanceof Long) {
            final long deliveryTag = (Long) msgId;
            if (amqpChannel != null) {
                try {
                    amqpChannel.basicReject(deliveryTag, false /* don't requeue */);
                } catch (IOException e) {
                    log.warn("Failed to reject delivery-tag " + deliveryTag, e);
                }
            }
        } else {
            log.warn(String.format("don't know how to reject(%s: %s)", msgId.getClass().getName(), msgId));
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(serialisationScheme.getOutputFields());
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

    /**
     *
     * @throws IOException
     * @throws TimeoutException
     */
    private void setupAMQP() throws IOException, TimeoutException {
        final int prefetchCount = this.prefetchCount;

        final ConnectionFactory connectionFactory = new ConnectionFactory();

        connectionFactory.setHost(amqpHost);
        connectionFactory.setPort(amqpPort);
        connectionFactory.setUsername(amqpUsername);
        connectionFactory.setPassword(amqpPassword);
        connectionFactory.setVirtualHost(amqpVhost);

        this.amqpConnection = connectionFactory.newConnection();
        this.amqpChannel = amqpConnection.createChannel();

        log.info("Setting basic.qos prefetch-count to " + prefetchCount);
        amqpChannel.basicQos(prefetchCount);

        final AMQP.Queue.DeclareOk queue = queueDeclaration.declare(amqpChannel);
        final String queueName = queue.getQueue();
        log.info("Consuming queue " + queueName);

        this.amqpConsumer = new QueueingConsumer(amqpChannel);
        this.amqpConsumerTag = amqpChannel.basicConsume(queueName, false /* no auto-ack */, amqpConsumer);
    }

    /**
     *
     */
    private void reconnect() {
        log.info("Reconnecting to AMQP broker...");
        try {
            setupAMQP();
        } catch (IOException e) {
            log.warn("Failed to reconnect to AMQP broker", e);
        } catch (TimeoutException e) {
            log.warn("Failed to reconnect to AMQP broker", e);
        }
    }
}
