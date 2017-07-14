package com.axisrooms.storm.util;

public interface Constants {

    String BROKERS                 = "localhost:9092";

    String CM_BASE_URL             = "https://staging.axisrooms.com";
    String LOCAL_BASE_URL          = "http://localhost:8080";
    String DEV_DIRECTORY           = "/usr/local/storm";

    String CONFIG_FILE             = "storm-config.yaml";

    String DAYWISE_PRICE_EP        = "/api/daywisePrice";
    String BULK_PRICE_EP           = "/api/bulkPriceUpdate";
    String DAYWISE_INVENTORY_EP    = "/api/daywiseInventory";
    String BULK_INVENTORY_EP       = "/api/inventory";
    String BLOCK_CHANNEL_EP        = "/api/blockChannel";
    String UNBLOCK_CHANNEL_EP      = "/api/unblockChannel";
    String FAILED_REQUESTS_EP      = "/api/failedRequests";

    String WORKERS_COUNT           = "topology.worker.count";

    String PMS_GROUP_ID            = "pms.group.id";

    String KAFKA_TOPOLOGY          = "KafkaTopology";

    String DAYWISE_PRICE_TOPIC     = "daywise.price.topic";
    String BULK_PRICE_TOPIC        = "bulk.price.topic";
    String DAYWISE_INVENTORY_TOPIC = "daywise.inventory.topic";
    String BULK_INVENTORY_TOPIC    = "bulk.inventory.topic";
    String BLOCK_CHANNEL_TOPIC     = "block.channel.topic";
    String UNBLOCK_CHANNEL_TOPIC   = "unblock.channel.topic";

    String DAYWISE_PRICE_SPOUT     = "daywisePriceSpout";
    String BULK_PRICE_SPOUT        = "bulkPriceSpout";
    String DAYWISE_INVENTORY_SPOUT = "daywiseInventorySpout";
    String BULK_INVENTORY_SPOUT    = "bulkInventorySpout";
    String BLOCK_CHANNEL_SPOUT     = "blockChannelSpout";
    String UNBLOCK_CHANNEL_SPOUT   = "unblockChannelSpout";

    String DAYWISE_PRICE_BOLT      = "daywisePriceBolt";
    String BULK_PRICE_BOLT         = "bulkPriceBolt";
    String DAYWISE_INVENTORY_BOLT  = "daywiseInventoryBolt";
    String BULK_INVENTORY_BOLT     = "bulkInventoryBolt";
    String BLOCK_CHANNEL_BOLT      = "blockChannelBolt";
    String UNBLOCK_CHANNEL_BOLT    = "unblockChannelBolt";

    String DAYWISE_PRICE_SPOUT_PH       = "daywise.price.spout.parallelism.hint";
    String BULK_PRICE_SPOUT_PH          = "bulk.price.spout.parallelism.hint";
    String DAYWISE_INVENTORY_SPOUT_PH   = "daywise.inventory.spout.parallelism.hint";
    String BULK_INVENTORY_SPOUT_PH      = "bulk.inventory.spout.parallelism.hint";
    String BLOCK_CHANNEL_SPOUT_PH       = "block.channel.spout.parallelism.hint";
    String UNBLOCK_CHANNEL_SPOUT_PH     = "unblock.channel.spout.parallelism.hint";

    String DAYWISE_PRICE_BOLT_PH        = "daywise.price.bolt.parallelism.hint";
    String BULK_PRICE_BOLT_PH           = "bulk.price.bolt.parallelism.hint";
    String DAYWISE_INVENTORY_BOLT_PH    = "daywise.inventory.bolt.parallelism.hint";
    String BULK_INVENTORY_BOLT_PH       = "bulk.inventory.bolt.parallelism.hint";
    String BLOCK_CHANNEL_BOLT_PH        = "block.channel.bolt.parallelism.hint";
    String UNBLOCK_CHANNEL_BOLT_PH      = "unblock.channel.bolt.parallelism.hint";

    String DAYWISE_PRICE_BOLT_TASKS     = "daywise.price.bolt.tasks";
    String BULK_PRICE_BOLT_TASKS        = "bulk.price.bolt.tasks";
    String DAYWISE_INVENTORY_BOLT_TASKS = "daywise.inventory.bolt.tasks";
    String BULK_INVENTORY_BOLT_TASKS    = "bulk.inventory.bolt.tasks";
    String BLOCK_CHANNEL_BOLT_TASKS     = "block.channel.bolt.tasks";
    String UNBLOCK_CHANNEL_BOLT_TASKS   = "unblock.channel.bolt.tasks";
}
