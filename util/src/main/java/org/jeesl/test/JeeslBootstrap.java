package org.jeesl.test;

import org.exlp.controller.handler.io.log.LoggerBootstrap;
import org.exlp.controller.handler.system.property.ConfigLoader;
import org.exlp.interfaces.system.property.Configuration;
import org.exlp.util.io.config.ExlpCentralConfigPointer;
import org.exlp.util.jx.JaxbUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JeeslBootstrap
{
	final static Logger logger = LoggerFactory.getLogger(JeeslBootstrap.class);
	
	public enum App {jeesl}
	
	public static org.exlp.interfaces.system.property.Configuration init()
	{
		return JeeslBootstrap.init("jeesl/system/property/jeesl.xml");
	}
	
	private static Configuration init(String configFile)
	{
		LoggerBootstrap.instance("cli-jeesl.log4j2.xml").path("jeesl/system/io/log").init();
	
		ExlpCentralConfigPointer ccp = ExlpCentralConfigPointer.instance(App.jeesl).jaxb(JaxbUtil.instance());
		Configuration config = ConfigLoader.instance().add(ccp.toPath("client")).add(configFile).wrap();
		
		return config;
	}
}