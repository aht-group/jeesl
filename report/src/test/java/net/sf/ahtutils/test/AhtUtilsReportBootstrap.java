package net.sf.ahtutils.test;

import org.exlp.controller.handler.io.log.LoggerBootstrap;
import org.exlp.util.io.log.LoggerInit;
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
		LoggerBootstrap.instance().path("jeesl/system/io/log").init();
						
		//ConfigLoader.add(configFile);
		//Configuration config = ConfigLoader.init();					
		logger.debug("Logger initialized");
		//return config;
	}

}
