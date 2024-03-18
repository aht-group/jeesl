package net.sf.ahtutils.test;

import org.exlp.controller.handler.io.log.LoggerBootstrap;
import org.exlp.util.io.log.LoggerInit;
import org.exlp.util.jx.JaxbUtil;
import org.jeesl.model.xml.JeeslNsPrefixMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilsMonitorTestBootstrap
{
	final static Logger logger = LoggerFactory.getLogger(UtilsMonitorTestBootstrap.class);
	
	public static void init()
	{
		LoggerBootstrap.instance().path("jeesl/system/io/log").init();
		JaxbUtil.setNsPrefixMapper(new JeeslNsPrefixMapper());
	}
}