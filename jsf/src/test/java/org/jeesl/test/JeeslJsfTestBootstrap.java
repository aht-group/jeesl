package org.jeesl.test;

import org.exlp.controller.handler.io.log.LoggerBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslJsfTestBootstrap
{
	final static Logger logger = LoggerFactory.getLogger(JeeslJsfTestBootstrap.class);
		
	public static void init()
	{
		LoggerBootstrap.instance().path("jeesl/system/io/log").init();
	}
}