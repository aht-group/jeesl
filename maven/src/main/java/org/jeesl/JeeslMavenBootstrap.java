package org.jeesl;

import org.exlp.controller.handler.io.log.LoggerBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslMavenBootstrap
{
	final static Logger logger = LoggerFactory.getLogger(JeeslMavenBootstrap.class);
	
	public static void init()
	{
//		AbstractUtilsMavenTst.setfTarget(new File("target"));
		
		LoggerBootstrap.instance().path("jeesl/system/io/log").init();
	}
}