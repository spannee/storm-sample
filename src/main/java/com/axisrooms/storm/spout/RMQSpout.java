package com.axisrooms.storm.spout;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.storm.shade.org.json.simple.JSONObject;
import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;

public class RMQSpout extends BaseRichSpout {
  private final MessageScheme scheme;

  private transient RabbitMQConsumer consumer;
  private transient SpoutOutputCollector collector;
  private transient int prefetchCount;

  private String streamId;

	  
	  public RMQSpout(MessageScheme scheme, String streamId){
	      this.scheme =scheme;
	      this.streamId = streamId;
	  }

  @Override
  public void open(final Map config,
                   final TopologyContext context,
                   final SpoutOutputCollector spoutOutputCollector) {
    ConsumerConfig consumerConfig = ConsumerConfig.getFromStormConfig(config);
    
    consumer = loadConsumer( consumerConfig);
    scheme.open(config, context);
    consumer.open();
    prefetchCount = consumerConfig.getPrefetchCount();
    collector = spoutOutputCollector;
  }

  protected RabbitMQConsumer loadConsumer(ConsumerConfig config) {
    return new RabbitMQConsumer(config.getConnectionConfig(),
                                config.getPrefetchCount(),
                                config.getQueueName(),
                                config.isRequeueOnFail()
                                );
  }

  @Override
  public void close() {
    consumer.close();
    scheme.close();
    super.close();
  }

  @Override
  public void nextTuple() {
    int count = 0;
    Message message;
    while (count < prefetchCount && (message = consumer.nextMessage()) != Message.NONE) {
      List<Object> tuple = extractTuple(message);
      if (!tuple.isEmpty()) {
        emit(tuple, message, collector);
        count++;
      }
    }
  }

  protected List<Integer> emit(List<Object> tuple,
                               Message message,
                               SpoutOutputCollector spoutOutputCollector) {
    return streamId == null ? spoutOutputCollector.emit(tuple, getDeliveryTag(message)) : 
      spoutOutputCollector.emit(streamId, tuple, getDeliveryTag(message));
  }

  private List<Object> extractTuple(Message message) {
    long deliveryTag = getDeliveryTag(message);
    try {
      List<Object> tuple = scheme.deserialize(message);
      if (tuple != null && !tuple.isEmpty()) {
        return tuple;
      }
    } catch (Exception e) {
    }
    consumer.deadLetter(deliveryTag);
    return Collections.emptyList();
  }

  @Override
  public void ack(Object msgId) {
    if (msgId instanceof Long) consumer.ack((Long) msgId);
  }

  @Override
  public void fail(Object msgId) {
    if (msgId instanceof Long) consumer.fail((Long) msgId);
  }

  @Override
  public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
    if(streamId == null){
      outputFieldsDeclarer.declare(scheme.getOutputFields());
    }else{
      outputFieldsDeclarer.declareStream(streamId, scheme.getOutputFields());
    }
  }

  @Override
  public void deactivate()
  {
    super.deactivate();
  }

  @Override
  public void activate()
  {
    super.activate();
  }

  protected long getDeliveryTag(Message message) {
    return ((Message.DeliveredMessage) message).getDeliveryTag();
  }
}