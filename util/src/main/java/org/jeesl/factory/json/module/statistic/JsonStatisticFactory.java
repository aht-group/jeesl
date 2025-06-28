package org.jeesl.factory.json.module.statistic;

import org.jeesl.model.json.module.statistic.JsonStatisticValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonStatisticFactory
{
	final static Logger logger = LoggerFactory.getLogger(JsonStatisticFactory.class);
	
	public static JsonStatisticValue build() {return new JsonStatisticValue();}
	
	public static JsonStatisticValue counter(String code, int value)
	{
		JsonStatisticValue json = JsonStatisticFactory.build();
		json.setCode(code);
		json.setCounter(value);
		
		return json;
	}
}