package org.jeesl.controller.io.ssi.wildfly.cache;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.jboss.as.controller.client.ModelControllerClient;
import org.jeesl.factory.json.io.ssi.core.JsonSsiCredentialFactory;
import org.jeesl.factory.json.io.ssi.core.JsonSsiSystemFactory;
import org.jeesl.model.json.io.ssi.core.JsonSsiSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractEapCacheConfigurator
{
	final static Logger logger = LoggerFactory.getLogger(AbstractEapCacheConfigurator.class);
	
	protected final ModelControllerClient client;
	
	public AbstractEapCacheConfigurator(ModelControllerClient client)
	{
		this.client=client;
	}
	
	public static JsonSsiSystem toSystem(org.exlp.interfaces.system.property.Configuration config, String context)
	{
		JsonSsiSystem system = JsonSsiSystemFactory.instance().fluent().code(context).credentials().json();
		try
		{
			String list = config.getString("cache."+context+".list");
	    	if(Objects.nonNull(list))
	    	{
	    		logger.info(list);
	    		system.setCredentials(new ArrayList<>());
	    		String[] caches  = list.split("-");
	    		for(String cache : caches)
	    		{
	    			system.getCredentials().add(JsonSsiCredentialFactory.instance().fluent().code(cache).json());
	    		}
	    	}
	    	else {logger.warn("No cache configuration for "+context);}
		}
		catch (NoSuchElementException e) {}
		
		return system;
	}
}