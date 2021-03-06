package org.jeesl;

import net.sf.exlp.util.config.ConfigLoader;
import net.sf.exlp.util.io.LoggerInit;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		LoggerInit loggerInit = new LoggerInit("log4j.xml");
		loggerInit.addAltPath("jeesl/util/config");
		loggerInit.init();
						
		ConfigLoader.add(configFile);
		Configuration config = ConfigLoader.init();					
		logger.debug("Config and Logger initialized");
		return config;
	}
}