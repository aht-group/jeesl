package org.jeesl;

import org.exlp.controller.handler.io.log.LoggerBootstrap;
import org.exlp.util.jx.JaxbUtil;
import org.jeesl.model.xml.JeeslNsPrefixMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslXmlTestBootstrap
{
	final static Logger logger = LoggerFactory.getLogger(JeeslXmlTestBootstrap.class);
		
	public static void init()
	{
		LoggerBootstrap.instance("cli.log4j2.xml").path("jeesl/system/io/log").init();
		logger.info("Logging Activated");
		System.out.println("System.out");
		
		JaxbUtil.setNsPrefixMapper(new JeeslNsPrefixMapper());
	}
}