package org.jeesl.factory.json.system.security.azure;

import org.jeesl.model.json.system.security.azure.JsonAzureUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonAzureUserFactory
{
	final static Logger logger = LoggerFactory.getLogger(JsonAzureUserFactory.class);
	
	private JsonAzureUser json;
	
	public static JsonAzureUserFactory instance() {return new JsonAzureUserFactory();}
	private JsonAzureUserFactory()
	{
		json = JsonAzureUserFactory.build(); 
	}
	
	
	public static JsonAzureUser build() {return new JsonAzureUser();}
	
	
	public JsonAzureUserFactory first(String value) {json.setFirstName(value); return this;}
	public JsonAzureUser toJson() {return json;}
}