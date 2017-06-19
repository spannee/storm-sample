package com.axisrooms.storm.rabbitMQ;

/**
 * Created by Santhosh on 6/13/17.
 */
public interface Constants {

    long DEFAULT_PREFETCH_COUNT     = 100;
    long WAIT_FOR_NEXT_MESSAGE      = 1L;
    long WAIT_AFTER_SHUTDOWN_SIGNAL = 10000L;

    String CONFIG_PREFETCH_COUNT   = "amqp.prefetch.count";
    String BROKERS                 = "localhost:9092";

    String KAFKA_SPOUT             = "kafkaSpout";
    String KAFKA_VALIDATOR_BOLT    = "kafkaValidatorBolt";
    String KAFKA_DEDUPLICATOR_BOLT = "kafkaDeduplicatorBolt";
    String KAFKA_PUBLISHER_BOLT    = "kafkaPublisherBolt";

    String RABBIT_ROUTING_KEY      = "testKey";
    String RABBIT_QUEUE_PRICE      = "TestQueueOne";
    String RABBIT_EXCHANGE         = "TestExchangeOne";
}
