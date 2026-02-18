package org.jeesl.factory.mqtt;

import java.util.Objects;

import org.exlp.util.io.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;

public class MqttHiveMessageFactory
{
	final static Logger logger = LoggerFactory.getLogger(MqttHiveMessageFactory.class);
	
	private String topic;
	private MqttQos qos;
	private boolean retained;
	
	private String valueString;
	private Double valueDouble;
	private Object valueJson;
	
	public static MqttHiveMessageFactory instance(String topic) {return new MqttHiveMessageFactory(topic);}
	private MqttHiveMessageFactory(String topic)
	{
		this.clear();
		this.topic = topic;
	}
	
	private void clear()
	{
		qos = MqttQos.AT_MOST_ONCE;
		retained = false;
		
		valueDouble = null;
		valueJson = null;
	}
	
	public MqttHiveMessageFactory value(double value) {this.valueDouble = value; return this;}
	public MqttHiveMessageFactory value(String value) {this.valueString = value; return this;}
	public MqttHiveMessageFactory json(Object json) {this.valueJson = json; return this;}
	
	public MqttHiveMessageFactory topic(String topic) {this.topic=topic; return this;}
	public MqttHiveMessageFactory retained(boolean retained) {this.retained = retained; return this;}
	
	public Mqtt5Publish build()
	{
		byte[] bytes = null;
		
		if(Objects.nonNull(valueString)) {bytes = valueString.getBytes();}
		else if(Objects.nonNull(valueDouble)) {bytes = JsonUtil.instance().toByte(valueDouble);}
		else if(Objects.nonNull(valueJson)) {bytes = JsonUtil.instance().toByte(valueJson);}
		
		 Mqtt5Publish publish = Mqtt5Publish.builder()
			        .topic(topic)
			        .payload(bytes)
			        .qos(qos)
			        .retain(retained)
			        .build();
		return publish;
	}
	
	public <E extends Enum<E>> Mqtt5PublishToBroker broker(E broker) {return Mqtt5PublishToBroker.of(broker, this.build());}
}