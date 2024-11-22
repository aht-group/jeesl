package org.jeesl.factory.json.system.security.azure;

import org.jeesl.model.json.system.security.azure.JsonAzure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonAzureFactory
{
	final static Logger logger = LoggerFactory.getLogger(JsonAzureFactory.class);
	

	
	
	public static JsonAzure build() {return new JsonAzure();}
	
	
}