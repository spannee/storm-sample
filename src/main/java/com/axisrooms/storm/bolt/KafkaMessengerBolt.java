package com.axisrooms.storm.bolt;

import com.axisrooms.storm.util.APIForwarder;
import com.axisrooms.storm.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.storm.shade.org.eclipse.jetty.http.HttpStatus;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class KafkaMessengerBolt implements IRichBolt {

    private OutputCollector collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple tuple) {
        int responseCode = 0;

        if (tuple.size()<=0 || tuple.getString(4)==null || tuple.getString(4).isEmpty())
            return;

        String jsonString = tuple.getString(4);
        log.info("The request - " + jsonString + " has been read");

        try {
            responseCode = APIForwarder.redirectPost(jsonString, Constants.CM_BASE_URL, Constants.DAYWISE_PRICE_EP);
            log.info("Response from CM - " + responseCode);
        } catch (IOException e) {
            log.error("Some exception occurred when we hit CM - " + e.getMessage());
        }

        if(!HttpStatus.isSuccess(responseCode)) {
            try {
                responseCode = APIForwarder.redirectPost(jsonString, Constants.LOCAL_BASE_URL, Constants.FAILED_REQUESTS_EP);
                log.info("Response from Kafka - " + responseCode);
            } catch (IOException e) {
                log.error("Some exception occurred when we tried to process failed messages - " + e.getMessage());
            }
        }

        this.collector.ack(tuple);
    }

    @Override
    public void cleanup() {

    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}