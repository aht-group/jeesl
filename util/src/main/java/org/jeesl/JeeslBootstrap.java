package org.jeesl;

import org.apache.commons.configuration.Configuration;
import org.exlp.controller.handler.system.property.ConfigLoader;
import org.jeesl.controller.handler.io.log.LoggerBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.io.LoggerInit;

public class JeeslBootstrap
{
	final static Logger logger = LoggerFactory.getLogger(JeeslBootstrap.class);
	
	public static Configuration init()
	{
		String configFile = "jeesl/util/config/jeesl.xml";
		return init(configFile);
	}
	
	public static Configuration init(String configFile)
	{
		LoggerBootstrap.instance("log4j2-jeesl.xml").path("jeesl/system/io/log").init();
		
//		LoggerInit loggerInit = new LoggerInit("log4j.xml");
//		loggerInit.path("jeesl/util/config");
//		loggerInit.init();
						
		ConfigLoader.addString(configFile);
		Configuration config = ConfigLoader.init();					
		logger.debug("Config and Logger initialized");
		return config;
	}
}