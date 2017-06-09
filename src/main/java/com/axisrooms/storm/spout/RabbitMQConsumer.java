package com.axisrooms.storm.spout;


import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;


public class RabbitMQConsumer implements Serializable {
  public static final long MS_WAIT_FOR_MESSAGE = 1L;

  private final ConnectionFactory connectionFactory;
  private final Address[] highAvailabilityHosts;
  private final int prefetchCount;
  private final String queueName;
  private final boolean requeueOnFail;

  private Connection connection;
  private Channel channel;
  private QueueingConsumer consumer;
  private String consumerTag;


  public RabbitMQConsumer(ConnectionConfig connectionConfig, int prefetchCount,
		String queueName, boolean requeueOnFail) {
	   this.connectionFactory = connectionConfig.asConnectionFactory();
	    this.highAvailabilityHosts = connectionConfig.getHighAvailabilityHosts().toAddresses();
	    this.prefetchCount = prefetchCount;
	    this.queueName = queueName;
	    this.requeueOnFail = requeueOnFail;
  }

public Message nextMessage() {
    reinitIfNecessary();
    if (consumerTag == null || consumer == null) return Message.NONE;
    try {
      return Message.forDelivery(consumer.nextDelivery(MS_WAIT_FOR_MESSAGE));
    } catch (ShutdownSignalException sse) {
      reset();
      return Message.NONE;
    } catch (InterruptedException ie) {
      return Message.NONE;
    } catch (ConsumerCancelledException cce) {
      reset();
      return Message.NONE;
    }
  }

  public void ack(Long msgId) {
    reinitIfNecessary();
    try {
      channel.basicAck(msgId, false);
    } catch (ShutdownSignalException sse) {
      reset();
    } catch (Exception e) {
    }
  }

  public void fail(Long msgId) {
    if (requeueOnFail)
      failWithRedelivery(msgId);
    else
      deadLetter(msgId);
  }

  public void failWithRedelivery(Long msgId) {
    reinitIfNecessary();
    try {
      channel.basicReject(msgId, true);
    } catch (ShutdownSignalException sse) {
      reset();
    } catch (Exception e) {
    }
  }

  public void deadLetter(Long msgId) {
    reinitIfNecessary();
    try {
      channel.basicReject(msgId, false);
    } catch (ShutdownSignalException sse) {
      reset();
    } catch (Exception e) {
    }
  }

  public void open() {
    try {
      connection = createConnection();
      channel = connection.createChannel();
      if (prefetchCount > 0) {
        channel.basicQos(prefetchCount);
      }

      consumer = new QueueingConsumer(channel);
      consumerTag = channel.basicConsume(queueName, isAutoAcking(), consumer);
    } catch (Exception e) {
      reset();
    }
  }

  protected boolean isAutoAcking() {
    return false;
  }

  public void close() {
    try {
      if (channel != null && channel.isOpen()) {
        if (consumerTag != null) channel.basicCancel(consumerTag);
        channel.close();
      }
    } catch (Exception e) {
    }
    try {
      connection.close();
    } catch (Exception e) {
    }
    consumer = null;
    consumerTag = null;
    channel = null;
    connection = null;
  }

  private void reset() {
    consumerTag = null;
  }

  private void reinitIfNecessary() {
    if (consumerTag == null || consumer == null) {
      close();
      open();
    }
  }

  private Connection createConnection() throws IOException, TimeoutException {
    Connection connection = highAvailabilityHosts == null || highAvailabilityHosts.length == 0 
          ? connectionFactory.newConnection() 
          : connectionFactory.newConnection(highAvailabilityHosts);
    connection.addShutdownListener(new ShutdownListener() {
      @Override
      public void shutdownCompleted(ShutdownSignalException cause) {
        reset();
      }
    });
    return connection;
  }
}