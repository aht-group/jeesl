package net.sf.ahtutils.test;

import org.apache.commons.configuration.Configuration;
import org.exlp.util.io.log.LoggerInit;
import org.exlp.util.jx.JaxbUtil;
import org.jeesl.model.xml.JeeslNsPrefixMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AhtUtilsDocBootstrap
{
	final static Logger logger = LoggerFactory.getLogger(AhtUtilsDocBootstrap.class);
	
	public static Configuration init()
	{
		String configFile = "ahtutils-util/ahtutils.xml";
		return init(configFile);
	}
	
	public static Configuration init(String configFile)
	{
		LoggerInit loggerInit = new LoggerInit("log4j.xml");
			loggerInit.path("config.ahtutils-doc.test");
			loggerInit.init();
						
//		ConfigLoader.add(configFile);
//		Configuration config = ConfigLoader.init();					
//		logger.debug("Config and Logger initialized");
		JaxbUtil.setNsPrefixMapper(new JeeslNsPrefixMapper());
			
		return null;
	}
}