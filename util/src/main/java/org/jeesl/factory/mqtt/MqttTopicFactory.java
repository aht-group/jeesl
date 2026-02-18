package org.jeesl.factory.mqtt;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttTopicFactory
{
	final static Logger logger = LoggerFactory.getLogger(MqttTopicFactory.class);
		
	public static String build(Serializable...attributes)
	{
		StringBuilder sb = new StringBuilder();
		
		for(int i=0;i<attributes.length;i++)
		{
			sb.append(attributes[i].toString());
			if(i<(attributes.length-1)) {sb.append("/");}
		}
		return sb.toString();
	}
	
	public static String append(String topic, Serializable suffix, int index)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(topic);
		sb.append("/").append(suffix).append(index);
		return sb.toString();
	}
}