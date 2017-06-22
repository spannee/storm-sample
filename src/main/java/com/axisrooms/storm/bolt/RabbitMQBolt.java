package com.axisrooms.storm.bolt;

import org.apache.log4j.Logger;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;

import java.util.Map;

/**
 * Created by Santhosh on 6/1/17.
 */
public class RabbitMQBolt implements IRichBolt {

    private static final Logger log = Logger.getLogger(RabbitMQBolt.class);

    private OutputCollector collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple tuple) {
        System.out.println("Hello here is the tuple - " + tuple.getString(0));
        collector.ack(tuple);
        log.info("Hello here is the tuple - " + tuple.getString(0));
    }

    @Override
    public void cleanup() {

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        //declarer.declare(new Fields("call", "duration"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
