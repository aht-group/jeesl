package org.jeesl;

import java.io.File;

import org.exlp.controller.handler.io.log.LoggerBootstrap;
import org.exlp.interfaces.io.NsPrefixMapperInterface;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractJeeslPrototypeTest 
{
	final static Logger logger = LoggerFactory.getLogger(AbstractJeeslPrototypeTest.class);
	
	protected static NsPrefixMapperInterface nsPrefixMapper;
	protected static File fTarget;
		
	@BeforeAll
    public static void initLogger()
	{
		if(!LoggerBootstrap.isLog4jInited())
		{
			LoggerBootstrap.instance().path("jeesl/system/io/log").init();
		}
    }
	
	@Test public void log4j()
	{
		
	}
}