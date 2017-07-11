package com.axisrooms.storm.util;

public interface Constants {

    String BROKERS                 = "localhost:9092";

    String KAFKA_SPOUT             = "kafkaSpout";
    String KAFKA_MESSENGER_BOLT    = "kafkaMessengerBolt";

    String CM_BASE_URL             = "https://staging.axisrooms.com";
    String LOCAL_BASE_URL          = "http://localhost:8080";

    String DAYWISE_PRICE_EP        = "/api/daywisePrice";
    String BULK_PRICE_EP           = "/api/bulkPriceUpdate";
    String FAILED_REQUESTS_EP      = "/api/failedRequests";
}
