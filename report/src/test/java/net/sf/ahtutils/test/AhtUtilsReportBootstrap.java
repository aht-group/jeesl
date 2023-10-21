package net.sf.ahtutils.test;

import net.sf.exlp.util.io.LoggerInit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AhtUtilsReportBootstrap {
	
final static Logger logger = LoggerFactory.getLogger(AhtUtilsReportBootstrap.class);
	
	public static void init()
	{
		// Not needed at the moment
		// String configFile = "ahtutils-report/ahtutils.xml";
		// return init(configFile);
	}
	
	public static void init(String configFile)
	{
		LoggerInit loggerInit = new LoggerInit("log4j.xml");
		loggerInit.path("ahtutils-report");
		loggerInit.init();
						
		//ConfigLoader.add(configFile);
		//Configuration config = ConfigLoader.init();					
		logger.debug("Logger initialized");
		//return config;
	}

}
