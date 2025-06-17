package org.jeesl.factory.mqtt;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MqttTopicFactory
{
	final static Logger logger = LoggerFactory.getLogger(MqttTopicFactory.class);
		
	public static String build(Serializable...attributes)
	{
		StringBuffer sb = new StringBuffer();
		
		for(int i=0;i<attributes.length;i++)
		{
			sb.append(attributes[i].toString());
			if(i<(attributes.length-1)) {sb.append("/");}
		}
		return sb.toString();
	}
}