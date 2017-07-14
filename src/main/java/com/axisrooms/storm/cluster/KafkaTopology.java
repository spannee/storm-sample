package com.axisrooms.storm.cluster;

import com.axisrooms.storm.bolt.*;
import com.axisrooms.storm.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.utils.Utils;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class KafkaTopology {

    public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException, IOException {
        Map<String, Object> stormConf = Utils.findAndReadConfigFile(Constants.DEV_DIRECTORY + Constants.CONFIG_FILE);

        Properties props = new Properties();
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, Utils.getBoolean(stormConf.get(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG), Boolean.TRUE));

        KafkaSpoutConfig<String, String> daywisePriceSpoutConfig = buildKafkaSpouts(Constants.DAYWISE_PRICE_TOPIC, props, stormConf);
        KafkaSpoutConfig<String, String> bulkPriceSpoutConfig = buildKafkaSpouts(Constants.BULK_PRICE_TOPIC, props, stormConf);
        KafkaSpoutConfig<String, String> daywiseInventorySpoutConfig = buildKafkaSpouts(Constants.DAYWISE_INVENTORY_TOPIC, props, stormConf);
        KafkaSpoutConfig<String, String> bulkInventorySpoutConfig = buildKafkaSpouts(Constants.BULK_INVENTORY_TOPIC, props, stormConf);
        KafkaSpoutConfig<String, String> blockChannelSpoutConfig = buildKafkaSpouts(Constants.BLOCK_CHANNEL_TOPIC, props, stormConf);
        KafkaSpoutConfig<String, String> unblockChannelSpoutConfig = buildKafkaSpouts(Constants.UNBLOCK_CHANNEL_TOPIC, props, stormConf);

        Config conf = buildConfig(stormConf);

        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout(Constants.DAYWISE_PRICE_SPOUT,  new KafkaSpout(daywisePriceSpoutConfig), Utils.getInt(stormConf.get(Constants.DAYWISE_PRICE_SPOUT_PH)));
        builder.setBolt(Constants.DAYWISE_PRICE_BOLT, new DaywisePriceBolt(), Utils.getInt(stormConf.get(Constants.DAYWISE_PRICE_BOLT_PH)))
                .shuffleGrouping(Constants.DAYWISE_PRICE_SPOUT)
                .setNumTasks(Utils.getInt(stormConf.get(Constants.DAYWISE_PRICE_BOLT_TASKS)));

        builder.setSpout(Constants.BULK_PRICE_SPOUT,  new KafkaSpout(bulkPriceSpoutConfig), Utils.getInt(stormConf.get(Constants.BULK_PRICE_SPOUT_PH)));
        builder.setBolt(Constants.BULK_PRICE_BOLT, new BulkPriceBolt(), Utils.getInt(stormConf.get(Constants.BULK_PRICE_BOLT_PH)))
                .shuffleGrouping(Constants.BULK_PRICE_SPOUT)
                .setNumTasks(Utils.getInt(stormConf.get(Constants.BULK_PRICE_BOLT_TASKS)));

        builder.setSpout(Constants.DAYWISE_INVENTORY_SPOUT,  new KafkaSpout(daywiseInventorySpoutConfig), Utils.getInt(stormConf.get(Constants.DAYWISE_INVENTORY_SPOUT_PH)));
        builder.setBolt(Constants.DAYWISE_INVENTORY_BOLT, new DaywiseInventoryBolt(), Utils.getInt(stormConf.get(Constants.DAYWISE_INVENTORY_BOLT_PH)))
                .shuffleGrouping(Constants.DAYWISE_INVENTORY_SPOUT)
                .setNumTasks( Utils.getInt(stormConf.get(Constants.DAYWISE_INVENTORY_BOLT_TASKS)));

        builder.setSpout(Constants.BULK_INVENTORY_SPOUT,  new KafkaSpout(bulkInventorySpoutConfig), Utils.getInt(stormConf.get(Constants.BULK_INVENTORY_SPOUT_PH)));
        builder.setBolt(Constants.BULK_INVENTORY_BOLT, new BulkInventoryBolt(), Utils.getInt(stormConf.get(Constants.BULK_INVENTORY_BOLT_PH)))
                .shuffleGrouping(Constants.BULK_INVENTORY_SPOUT)
                .setNumTasks(Utils.getInt(stormConf.get(Constants.BULK_INVENTORY_BOLT_TASKS)));

        builder.setSpout(Constants.BLOCK_CHANNEL_SPOUT,  new KafkaSpout(blockChannelSpoutConfig), Utils.getInt(stormConf.get(Constants.BLOCK_CHANNEL_SPOUT_PH)));
        builder.setBolt(Constants.BLOCK_CHANNEL_BOLT, new BlockChannelBolt(), Utils.getInt(stormConf.get(Constants.BLOCK_CHANNEL_BOLT_PH)))
                .shuffleGrouping(Constants.BLOCK_CHANNEL_SPOUT)
                .setNumTasks(Utils.getInt(stormConf.get(Constants.BLOCK_CHANNEL_BOLT_TASKS)));

        builder.setSpout(Constants.UNBLOCK_CHANNEL_SPOUT,  new KafkaSpout(unblockChannelSpoutConfig), Utils.getInt(stormConf.get(Constants.UNBLOCK_CHANNEL_SPOUT_PH)));
        builder.setBolt(Constants.UNBLOCK_CHANNEL_BOLT, new UnblockChannelBolt(), Utils.getInt(stormConf.get(Constants.UNBLOCK_CHANNEL_BOLT_PH)))
                .shuffleGrouping(Constants.UNBLOCK_CHANNEL_SPOUT)
                .setNumTasks(Utils.getInt(stormConf.get(Constants.UNBLOCK_CHANNEL_BOLT_TASKS)));

        StormSubmitter.submitTopology(Constants.KAFKA_TOPOLOGY, conf, builder.createTopology());
    }

    /**
     *
     * @param topicName
     * @param props
     * @param stormConf
     * @return
     */
    private static KafkaSpoutConfig<String, String> buildKafkaSpouts(String topicName, Properties props, Map<String, Object> stormConf) {
        KafkaSpoutConfig<String, String> spoutConfig = KafkaSpoutConfig.builder(Constants.BROKERS, Utils.getString(stormConf.get(topicName)))
                .setFirstPollOffsetStrategy(KafkaSpoutConfig.FirstPollOffsetStrategy.UNCOMMITTED_EARLIEST).setGroupId(Utils.getString(stormConf.get(Constants.PMS_GROUP_ID)))
                .setProp(props).build();

        return spoutConfig;
    }

    /**
     *
     * @param stormConf
     * @return
     */
    private static Config buildConfig(Map<String, Object> stormConf) {
        Config conf = new Config();
        conf.setNumWorkers(Utils.getInt(stormConf.get(Constants.WORKERS_COUNT)));
        conf.put(Config.TOPOLOGY_WORKER_CHILDOPTS, Utils.getString(stormConf.get(Config.TOPOLOGY_WORKER_CHILDOPTS)));
        conf.setMaxSpoutPending(Utils.getInt(stormConf.get(Config.TOPOLOGY_MAX_SPOUT_PENDING)));
        conf.setDebug(Utils.getBoolean(stormConf.get(Config.TOPOLOGY_DEBUG), Boolean.FALSE));

        return conf;
    }
}
