package com.axisrooms.storm.bolt;

import com.axisrooms.storm.util.MongoSingleton;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.apache.log4j.Logger;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.net.UnknownHostException;
import java.util.Map;

/**
 * Created by Santhosh on 6/15/17.
 */
public class KafkaDeduplicatorBolt implements IRichBolt {

    private static final Logger log = Logger.getLogger(KafkaDeduplicatorBolt.class);

    private OutputCollector collector;

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    @Override
    public void execute(Tuple tuple) {
        if(tuple.size() <= 0 || tuple.getString(0)==null || tuple.getString(0).isEmpty())
            return;

        String jsonRequest = tuple.getString(0);

        String ip = "localhost";
        MongoSingleton mongo = null;
        try {
            mongo = MongoSingleton.getInstance(ip, 27017);
        } catch (UnknownHostException e) {
            log.info("Some exception occurred with mongo - " + e.getMessage());
            e.printStackTrace();
        }

        MongoDatabase db = mongo.getDatabase("mongo-local");
        //db.createCollection("priceRequests");

        MongoCollection<Document> collection = db.getCollection("priceRequests");

        Bson filter = new Document("_id", jsonRequest.hashCode());// Retrieve it
        Document result = collection.find(filter).first();

        if(result == null) {
            Document document = new Document("_id", jsonRequest.hashCode());
            document.append("value", jsonRequest);
            collection.insertOne(document);
            log.info("Inserted the following request - " + jsonRequest);

            collector.emit(new Values(tuple.getString(0)));
        } else if(result.size() > 0) {
            if(result.get("value").equals(jsonRequest)) {
                log.info("Already there");
            }
        }
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
