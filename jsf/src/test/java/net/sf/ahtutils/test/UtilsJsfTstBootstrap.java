package net.sf.ahtutils.test;

import net.sf.exlp.util.io.LoggerInit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UtilsJsfTstBootstrap
{
	final static Logger logger = LoggerFactory.getLogger(UtilsJsfTstBootstrap.class);
		
	public static void init()
	{
		LoggerInit loggerInit = new LoggerInit("log4j.xml");	
		loggerInit.path("config.ahtutils-jsf.test");
		loggerInit.init();		
	}
}