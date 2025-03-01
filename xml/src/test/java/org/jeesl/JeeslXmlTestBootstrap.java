package org.jeesl;

import org.exlp.controller.handler.io.log.LoggerBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslXmlTestBootstrap
{
	final static Logger logger = LoggerFactory.getLogger(JeeslXmlTestBootstrap.class);
		
	public static void init()
	{
		LoggerBootstrap.instance().path("jeesl/system/io/log").init();
		
//		JaxbUtil.setNsPrefixMapper(new JeeslNsPrefixMapper());
	}
}