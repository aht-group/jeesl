package org.jeesl.factory.json.io.ssi.crypto;

import org.jeesl.model.json.io.crypto.JsonCryptoData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonCryptoDataFactory
{
	final static Logger logger = LoggerFactory.getLogger(JsonCryptoDataFactory.class);
	
	public static JsonCryptoData build(){return new JsonCryptoData();}
	
	public static <E extends Enum<E>> JsonCryptoData build(E code, String value)
	{
		JsonCryptoData json = build();
		json.setCode(code.toString());
		json.setValue(value);
		return json;
	}
}