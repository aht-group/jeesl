package org.jeesl.factory.json.io.ssi.data;

import org.jeesl.interfaces.model.io.ssi.data.JeeslIoSsiError;
import org.jeesl.model.json.io.crypto.JsonCryptoData;
import org.jeesl.model.json.io.ssi.data.JsonSsiError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonSsiErrorFactory<ERROR extends JeeslIoSsiError<?,?,?,?>>
{
	final static Logger logger = LoggerFactory.getLogger(JsonSsiErrorFactory.class);
	
	public static <ERROR extends JeeslIoSsiError<?,?,?,?>> JsonSsiErrorFactory<ERROR> instance() {return new JsonSsiErrorFactory<>();}
	private JsonSsiErrorFactory()
	{
		
	}
	
	
	public static JsonCryptoData build(){return new JsonCryptoData();}
	
	public JsonSsiError build(ERROR error)
	{
		JsonSsiError json = new JsonSsiError();
		json.setCode(error.getCode());
		return json;
	}
}