package net.sf.ahtutils.test;

import org.exlp.controller.handler.io.log.LoggerBootstrap;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractUtilsMonitorTest
{
	final static Logger logger = LoggerFactory.getLogger(AbstractUtilsMonitorTest.class);
	
	@BeforeAll
    public static void initLogger()
	{
		if(!LoggerBootstrap.isLog4jInited())
		{
			LoggerBootstrap.instance().path("jeesl/system/io/log").init();
		}
    }
	
	@Test public void dummy() {}
}