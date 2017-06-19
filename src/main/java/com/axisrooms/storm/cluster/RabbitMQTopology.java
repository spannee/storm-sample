package com.axisrooms.storm.cluster;

import com.axisrooms.storm.bolt.RabbitMQBolt;
import com.axisrooms.storm.rabbitMQ.QueueDeclaration;
import com.axisrooms.storm.rabbitMQ.SharedQueueWithBinding;
import com.axisrooms.storm.spout.RabbitMQSpout;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.spout.Scheme;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by Santhosh on 6/13/17.
 */
public class RabbitMQTopology {

    public static void main(String[] args) {
        TopologyBuilder builder = new TopologyBuilder();

        QueueDeclaration qd = new SharedQueueWithBinding("TestQueueOne", "TestExchangeOne", "testKey");

        Scheme scheme = new CustomScheme();

        builder.setSpout( "spout", new RabbitMQSpout(   "127.0.0.1", 5672, "guest", "guest", "/", qd, scheme));
        builder.setBolt( "testBolt", new RabbitMQBolt() )
                .shuffleGrouping("spout");

        Config conf = new Config();
        conf.setDebug(true);
        conf.setNumWorkers(1);
        conf.setMaxSpoutPending(5000);

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("RabbitMQTopology", conf, builder.createTopology());

        cluster.shutdown();
    }

    private static class CustomScheme implements Scheme {

        @Override
        public List<Object> deserialize(ByteBuffer ser) {
            try {
                return new Values(new String(ser.array(), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Fields getOutputFields() {
            return new Fields("");
        }
    }
}
