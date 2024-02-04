package org.jeesl;

import org.exlp.util.io.log.LoggerInit;
import org.exlp.util.jx.JaxbUtil;
import org.jeesl.model.xml.JeeslNsPrefixMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslXmlTestBootstrap
{
	final static Logger logger = LoggerFactory.getLogger(JeeslXmlTestBootstrap.class);
		
	public static void init()
	{
		LoggerInit loggerInit = new LoggerInit("log4j.xml");	
		loggerInit.path("config.ahtutils-xml.test");
		loggerInit.init();
		
		JaxbUtil.setNsPrefixMapper(new JeeslNsPrefixMapper());
	}
}