package org.jeesl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.io.LoggerInit;

public class JeeslMavenBootstrap
{
	final static Logger logger = LoggerFactory.getLogger(JeeslMavenBootstrap.class);
	
	public static void init()
	{
//		AbstractUtilsMavenTst.setfTarget(new File("target"));
		
		LoggerInit loggerInit = new LoggerInit("log4j.maven.xml");
		loggerInit.addAltPath("jeesl/system/io/config");
		loggerInit.init();
	}
}