package org.jeesl.factory.json.module.survey;

import org.jeesl.model.json.module.survey.data.JsonCorrelation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonCorrelationFactory
{
	final static Logger logger = LoggerFactory.getLogger(JsonCorrelationFactory.class);
	
	public static JsonCorrelation build(){return new JsonCorrelation();}
	
	public static JsonCorrelation build(long id)
	{
		JsonCorrelation json = build();
		json.setId(id);
		return json;
	}
}