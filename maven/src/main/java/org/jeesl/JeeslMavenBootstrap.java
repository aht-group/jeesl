package org.jeesl;

import org.exlp.util.io.log.LoggerInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslMavenBootstrap
{
	final static Logger logger = LoggerFactory.getLogger(JeeslMavenBootstrap.class);
	
	public static void init()
	{
//		AbstractUtilsMavenTst.setfTarget(new File("target"));
		
		LoggerInit loggerInit = new LoggerInit("log4j.maven.xml");
		loggerInit.path("jeesl/system/io/config");
		loggerInit.init();
	}
}