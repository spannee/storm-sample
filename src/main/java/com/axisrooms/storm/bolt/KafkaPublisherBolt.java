package com.axisrooms.storm.bolt;

import com.axisrooms.storm.util.Constants;
import com.rabbitmq.client.*;
import org.apache.log4j.Logger;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * Created by Santhosh on 6/16/17.
 */
public class KafkaPublisherBolt implements IRichBolt {

    private static final Logger log = Logger.getLogger(KafkaPublisherBolt.class);

    private OutputCollector collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple tuple) {
        //System.out.println("Hello here is the tuple - " + tuple.getString(0));
        //collector.ack(tuple);

        String jsonRequest = "";

        if(tuple.size()>0 && tuple.getString(0) != null) {
            jsonRequest = tuple.getString(0);

            if (!jsonRequest.isEmpty()) {
                log.info("Hello here is the tuple - " + tuple.getString(0));

                if (publishRequest(jsonRequest)) {
                    this.collector.ack(tuple);
                    log.info("The tuple has been pushed to rabbitMQ");

                }
            }
        }
    }

    public boolean publishRequest(String jsonRequest) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try {
            Connection connection = factory.newConnection();

            Channel channel = connection.createChannel();

            //Create an exchange
            channel.exchangeDeclare(Constants.RABBIT_EXCHANGE, "direct");

            //Set the maximum priority for queues
            Map<String, Object> priorityArgs = new HashMap<>();
            priorityArgs.put("x-max-priority", 255);
            channel.queueDeclare(Constants.RABBIT_QUEUE_PRICE, true, false, false, priorityArgs);

            //Bind exchange with queue
            channel.queueBind(Constants.RABBIT_QUEUE_PRICE, Constants.RABBIT_EXCHANGE, Constants.RABBIT_ROUTING_KEY);

            //Set priority
            //final AMQP.BasicProperties props = MessageProperties.PERSISTENT_BASIC.builder().priority(localIdx+10).build();

            //Publish message to the queue
            channel.basicPublish("", Constants.RABBIT_QUEUE_PRICE, null, jsonRequest.getBytes());

        } catch(IOException e) {
            log.error("An IOException occurred while publishing the request - " + e.getMessage());
            return false;
        } catch(TimeoutException e) {
            log.error("A TimeoutException occurred while publishing the request - " + e.getMessage());
            return false;
        }

        return true;
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
