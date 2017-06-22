package com.axisrooms.storm.bolt;

import com.axisrooms.storm.util.APIIdKeyMapper;
import com.axisrooms.storm.util.Constants;
import org.apache.log4j.Logger;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;

/**
 * Created by Santhosh on 6/1/17.
 */
public class KafkaValidatorBolt implements IRichBolt {

    private static final Logger log = Logger.getLogger(KafkaValidatorBolt.class);

    private OutputCollector collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple tuple) {
        String channelId = "";
        String jsonString = "";

        org.json.JSONObject jsonObject;

        APIIdKeyMapper channel;

        if (tuple.size()<=0 || tuple.getString(4)==null || tuple.getString(4).isEmpty())
            return;

        jsonString = tuple.getString(4);
        jsonObject = new org.json.JSONObject(jsonString);

        channelId = jsonObject.get(Constants.CHANNEL_ID).toString();
        if (channelId!=null && !channelId.isEmpty()) {
            channel = APIIdKeyMapper.valueOf(Long.parseLong(channelId));
            if (channel == null)
                return;
        } else {
            return;
        }

        String key = jsonObject.get(Constants.ACCESS_KEY).toString();
        if (!channel.getKey().equals(key)) {
            return;
        }

        jsonObject.remove(Constants.CHANNEL_ID);
        jsonObject.remove(Constants.ACCESS_KEY);

        collector.emit(new Values(jsonObject.toString()));
    }

    @Override
    public void cleanup() {

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("jsonRequest"));
     }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
