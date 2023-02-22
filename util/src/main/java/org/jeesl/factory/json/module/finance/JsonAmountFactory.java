package org.jeesl.factory.json.module.finance;

import org.jeesl.model.json.module.finance.JsonAmount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonAmountFactory
{
	final static Logger logger = LoggerFactory.getLogger(JsonAmountFactory.class);
	
	public static JsonAmount build(){return new JsonAmount();}
	
	public static JsonAmount build(double value, String currency)
	{
		JsonAmount json = new JsonAmount();
		json.setValue(value);
		json.setCurrency(JsonCurrencyFactory.build(currency));
		return json;
	}
}