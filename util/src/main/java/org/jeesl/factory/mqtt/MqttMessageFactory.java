package org.jeesl.factory.mqtt;

import java.util.Objects;

import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.exlp.util.io.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttMessageFactory
{
	final static Logger logger = LoggerFactory.getLogger(MqttMessageFactory.class);
	
	private int qos;
	private boolean retained;
	
	private String valueString;
	private Double valueDouble;
	private Object valueJson;
	
	public static MqttMessageFactory instance() {return new MqttMessageFactory();}
	public MqttMessageFactory()
	{
		this.clear();
	}
	
	private void clear()
	{
		qos = 0;
		retained = false;
		
		valueDouble = null;
		valueJson = null;
	}
	
	public MqttMessageFactory value(double value) {this.valueDouble = value; return this;}
	public MqttMessageFactory value(String value) {this.valueString = value; return this;}
	public MqttMessageFactory json(Object json) {this.valueJson = json; return this;}
	
	public MqttMessageFactory retained(boolean retained) {this.retained = retained; return this;}
	
	public MqttMessage build()
	{
		byte[] bytes = null;
		
		if(Objects.nonNull(valueString)) {bytes = valueString.getBytes();}
		else if(Objects.nonNull(valueDouble)) {bytes = JsonUtil.instance().toByte(valueDouble);}
		else if(Objects.nonNull(valueJson)) {bytes = JsonUtil.instance().toByte(valueJson);}
		
		MqttMessage msg = new MqttMessage(bytes);
		msg.setQos(qos);
		msg.setRetained(retained);
		return msg;
	}
}