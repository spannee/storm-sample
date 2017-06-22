package com.axisrooms.storm.cluster;

import com.axisrooms.storm.bolt.KafkaDeduplicatorBolt;
import com.axisrooms.storm.bolt.KafkaPublisherBolt;
import com.axisrooms.storm.bolt.KafkaValidatorBolt;
import com.axisrooms.storm.util.Constants;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.storm.Config;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

import java.util.Properties;

/**
 * Created by Santhosh on 6/15/17.
 */
public class KafkaTopology {


    public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        Properties props = new Properties();
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "100");

        KafkaSpoutConfig<String, String> kafkaSpoutConfig = KafkaSpoutConfig.builder(Constants.BROKERS, "dayWisePrice")
                .setFirstPollOffsetStrategy(KafkaSpoutConfig.FirstPollOffsetStrategy.UNCOMMITTED_EARLIEST).setGroupId("prices")
                .build();

        TopologyBuilder builder = new TopologyBuilder();
        Config conf = new Config();
        conf.setNumWorkers(1);
        conf.setMaxSpoutPending(1000);
        conf.setDebug(true);

        builder.setSpout(Constants.KAFKA_SPOUT,  new KafkaSpout(kafkaSpoutConfig), 1);
        builder.setBolt(Constants.KAFKA_VALIDATOR_BOLT, new KafkaValidatorBolt(), 1).shuffleGrouping(Constants.KAFKA_SPOUT);
        builder.setBolt(Constants.KAFKA_DEDUPLICATOR_BOLT, new KafkaDeduplicatorBolt(), 1).fieldsGrouping(Constants.KAFKA_VALIDATOR_BOLT, new Fields("jsonRequest"));
        builder.setBolt(Constants.KAFKA_PUBLISHER_BOLT, new KafkaPublisherBolt(), 1).fieldsGrouping(Constants.KAFKA_DEDUPLICATOR_BOLT, new Fields("jsonRequest"));

        StormSubmitter.submitTopology("KafkaTopology", conf,   builder.createTopology());

        //LocalCluster cluster = new LocalCluster();
        //cluster.submitTopology("KafkaTopology", conf, builder.createTopology());

        //cluster.shutdown();
    }
}
