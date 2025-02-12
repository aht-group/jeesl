package org.jeesl.test;

import org.apache.commons.configuration.Configuration;
import org.exlp.controller.handler.io.log.LoggerBootstrap;
import org.exlp.controller.handler.system.property.ConfigLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslBootstrap
{
	final static Logger logger = LoggerFactory.getLogger(JeeslBootstrap.class);
	
	public static org.exlp.interfaces.system.property.Configuration wrap() {return ConfigLoader.wrap(init());}
	public static Configuration init()
	{
		String configFile = "jeesl/util/config/jeesl.xml";
		return init(configFile);
	}
	
	public static Configuration init(String configFile)
	{
		LoggerBootstrap.instance("cli-jeesl.log4j2.xml").path("jeesl/system/io/log").init();
						
		ConfigLoader.addString(configFile);
		Configuration config = ConfigLoader.init();					
		logger.debug("Config and Logger initialized");
		return config;
	}
}