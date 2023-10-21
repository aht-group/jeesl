package net.sf.ahtutils.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.io.LoggerInit;

public class AbstractUtilsMonitorTest
{
	final static Logger logger = LoggerFactory.getLogger(AbstractUtilsMonitorTest.class);
	
	@BeforeAll
    public static void initLogger()
	{
		if(!LoggerInit.isLog4jInited())
		{
			LoggerInit loggerInit = new LoggerInit("log4junit.xml");	
			loggerInit.path("config.ahtutils-monitor.test");
			loggerInit.init();
		}
    }
	
	@Test public void dummy() {}
}