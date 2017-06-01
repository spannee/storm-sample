package com.axisrooms.storm.spout;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichSpout;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by Santhosh on 6/1/17.
 */
public class CallLogReaderSpout implements IRichSpout {

    private SpoutOutputCollector collector;

    private TopologyContext context;

    private Random randomGenerator = new Random();
    private Integer idx = 0;

    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        this.context = context;
        this.collector = collector;
    }

    @Override
    public void close() {

    }

    @Override
    public void activate() {

    }

    @Override
    public void deactivate() {

    }

    @Override
    public void nextTuple() {
        if(this.idx <= 1000) {
            List<String> mobileNumbers = new ArrayList<String>();
            mobileNumbers.add("1234123401");
            mobileNumbers.add("1234123402");
            mobileNumbers.add("1234123403");
            mobileNumbers.add("1234123404");

            Integer localIdx = 0;
            while(localIdx++ < 100 && this.idx++ < 1000) {
                String fromMobileNumber = mobileNumbers.get(randomGenerator.nextInt(4));
                String toMobileNumber = mobileNumbers.get(randomGenerator.nextInt(4));

                while(fromMobileNumber == toMobileNumber) {
                    toMobileNumber = mobileNumbers.get(randomGenerator.nextInt(4));
                }

                Integer duration = randomGenerator.nextInt(60);
                this.collector.emit(new Values(fromMobileNumber, toMobileNumber, duration));
            }
        }
    }

    @Override
    public void ack(Object msgId) {

    }

    @Override
    public void fail(Object msgId) {

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("from", "to", "duration"));
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
