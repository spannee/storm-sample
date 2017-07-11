package com.axisrooms.storm.cluster;

import com.axisrooms.storm.bolt.BulkPriceBolt;
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

import java.util.Properties;

@Slf4j
public class KafkaTopology {

    public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        Properties props = new Properties();
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");

        KafkaSpoutConfig<String, String> kafkaSpoutConfig = KafkaSpoutConfig.builder(Constants.BROKERS, "bulkPriceUpdate")
                .setFirstPollOffsetStrategy(KafkaSpoutConfig.FirstPollOffsetStrategy.UNCOMMITTED_EARLIEST).setGroupId("pmsTopics")
                .build();

        TopologyBuilder builder = new TopologyBuilder();
        Config conf = new Config();
        conf.setNumWorkers(1);
        conf.put(Config.TOPOLOGY_WORKER_CHILDOPTS, "-Xmn2048m");

        conf.setMaxSpoutPending(1000);
        conf.setDebug(true);

        builder.setSpout(Constants.KAFKA_SPOUT,  new KafkaSpout(kafkaSpoutConfig), 1);
        builder.setBolt(Constants.KAFKA_MESSENGER_BOLT, new BulkPriceBolt(), 1).shuffleGrouping(Constants.KAFKA_SPOUT);

        StormSubmitter.submitTopology("KafkaTopology", conf,   builder.createTopology());
    }
}
