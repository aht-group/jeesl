package net.sf.ahtutils.test;

import org.apache.commons.configuration.Configuration;
import org.exlp.controller.handler.io.log.LoggerBootstrap;
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
		LoggerBootstrap.instance().path("jeesl/system/io/log").init();
						
//		ConfigLoader.add(configFile);
//		Configuration config = ConfigLoader.init();					
//		logger.debug("Config and Logger initialized");
		JaxbUtil.setNsPrefixMapper(new JeeslNsPrefixMapper());
			
		return null;
	}
}