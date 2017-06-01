package com.axisrooms.storm;

import com.axisrooms.storm.bolt.CallLogCounterBolt;
import com.axisrooms.storm.bolt.CallLogCreatorBolt;
import com.axisrooms.storm.spout.CallLogReaderSpout;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

/**
 * Created by Santhosh on 6/1/17.
 */
public class LogAnalyserStorm {

    public static void main(String[] args) throws Exception{
        Config config = new Config();
        config.setDebug(true);

        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("call-log-reader-spout", new CallLogReaderSpout());

        builder.setBolt("call-log-creator-bolt", new CallLogCreatorBolt())
                .shuffleGrouping("call-log-reader-spout");

        builder.setBolt("call-log-counter-bolt", new CallLogCounterBolt())
                .fieldsGrouping("call-log-creator-bolt", new Fields("call"));

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("LogAnalyserStorm", config, builder.createTopology());
        Thread.sleep(10000);

        cluster.shutdown();
    }

}
