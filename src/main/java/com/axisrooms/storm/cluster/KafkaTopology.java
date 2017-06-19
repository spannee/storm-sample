package com.axisrooms.storm.cluster;

import com.axisrooms.storm.bolt.KafkaDeduplicatorBolt;
import com.axisrooms.storm.bolt.KafkaPublisherBolt;
import com.axisrooms.storm.bolt.KafkaValidatorBolt;
import com.axisrooms.storm.rabbitMQ.Constants;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

/**
 * Created by Santhosh on 6/15/17.
 */
public class KafkaTopology {


    public static void main(String[] args) {

        KafkaSpoutConfig<String, String> kafkaSpoutConfig = KafkaSpoutConfig.builder(Constants.BROKERS, "testTwo")
                .setFirstPollOffsetStrategy(KafkaSpoutConfig.FirstPollOffsetStrategy.EARLIEST).setGroupId("random")
                .build();

        TopologyBuilder builder = new TopologyBuilder();
        Config conf = new Config();
        conf.setNumWorkers(1);
        conf.setDebug(true);

        builder.setSpout(Constants.KAFKA_SPOUT,  new KafkaSpout(kafkaSpoutConfig), 1);
        builder.setBolt(Constants.KAFKA_VALIDATOR_BOLT, new KafkaValidatorBolt(), 1).shuffleGrouping(Constants.KAFKA_SPOUT);
        builder.setBolt(Constants.KAFKA_DEDUPLICATOR_BOLT, new KafkaDeduplicatorBolt(), 1).fieldsGrouping(Constants.KAFKA_VALIDATOR_BOLT, new Fields("jsonRequest"));
        builder.setBolt(Constants.KAFKA_PUBLISHER_BOLT, new KafkaPublisherBolt(), 1).fieldsGrouping(Constants.KAFKA_DEDUPLICATOR_BOLT, new Fields("jsonRequest"));

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("KafkaTopology", conf, builder.createTopology());

        //cluster.shutdown();
    }
}
