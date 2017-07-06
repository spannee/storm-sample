package com.axisrooms.storm.util;

/**
 * Created by Santhosh on 6/29/17.
 */
public interface Constants {

    String BROKERS                 = "localhost:9092";

    String KAFKA_SPOUT             = "kafkaSpout";
    String KAFKA_MESSENGER_BOLT    = "kafkaValidatorBolt";

    String CM_BASE_URL             = "https://staging.axisrooms.com";
    String LOCAL_BASE_URL          = "http://localhost:8080";

    String DAYWISE_PRICE_EP        = "/api/daywisePrice";
    String FAILED_REQUESTS_EP      = "/api/failedRequests";
}
