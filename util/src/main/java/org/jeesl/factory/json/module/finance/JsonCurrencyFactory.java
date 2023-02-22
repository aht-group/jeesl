package org.jeesl.factory.json.module.finance;

import org.jeesl.model.json.module.finance.JsonCurrency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonCurrencyFactory
{
	final static Logger logger = LoggerFactory.getLogger(JsonCurrencyFactory.class);
	
	public static JsonCurrency build(){return new JsonCurrency();}
	
	public static JsonCurrency build(String code)
	{
		JsonCurrency json = new JsonCurrency();
		json.setCode(code);
		return json;
	}
}