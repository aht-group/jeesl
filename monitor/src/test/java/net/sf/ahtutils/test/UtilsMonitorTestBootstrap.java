package net.sf.ahtutils.test;

import net.sf.exlp.util.io.LoggerInit;
import net.sf.exlp.util.xml.JaxbUtil;

import org.jeesl.model.xml.JeeslNsPrefixMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilsMonitorTestBootstrap
{
	final static Logger logger = LoggerFactory.getLogger(UtilsMonitorTestBootstrap.class);
	
	public static void init()
	{
		LoggerInit loggerInit = new LoggerInit("log4j.xml");
		loggerInit.path("config.ahtutils-monitor.test");
		loggerInit.init();
		JaxbUtil.setNsPrefixMapper(new JeeslNsPrefixMapper());
	}
}