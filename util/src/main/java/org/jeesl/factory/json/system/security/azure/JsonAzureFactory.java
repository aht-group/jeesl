package org.jeesl.factory.json.system.security.azure;

import java.util.ArrayList;
import java.util.Arrays;

import org.jeesl.model.json.system.security.azure.JsonAzure;
import org.jeesl.model.json.system.security.azure.JsonAzureUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonAzureFactory
{
	final static Logger logger = LoggerFactory.getLogger(JsonAzureFactory.class);
	

	
	
	public static JsonAzure build() {return new JsonAzure();}
	public static JsonAzure build(JsonAzureUser[] array)
	{
		JsonAzure json = JsonAzureFactory.build();
		json.setUsers(new ArrayList<>(Arrays.asList(array)));
		return json;
	}
	
	
}