package org.jeesl.test;

import org.exlp.controller.handler.io.log.LoggerBootstrap;
import org.exlp.interfaces.system.property.Configuration;
import org.exlp.util.io.config.ExlpCentralConfigPointer;
import org.exlp.util.jx.JaxbUtil;
import org.jeesl.controller.handler.system.property.ConfigBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslDocBootstrap
{
	final static Logger logger = LoggerFactory.getLogger(JeeslDocBootstrap.class);
	
	public enum App {jeesl}
	
	public static Configuration init()
	{
		String configFile = "ahtutils-util/ahtutils.xml";
		return init(configFile);
	}
	
	private static Configuration init(String configFile)
	{
		LoggerBootstrap.instance().path("jeesl/system/io/log").init();
		
		ExlpCentralConfigPointer ccp = ExlpCentralConfigPointer.instance(App.jeesl).jaxb(JaxbUtil.instance());
		ConfigBootstrap bootstrap = ConfigBootstrap.instance();
		bootstrap.add(ccp.toPath("showcase"));
		bootstrap.add(configFile);
		return ConfigBootstrap.wrap(bootstrap.combine());
	}
}