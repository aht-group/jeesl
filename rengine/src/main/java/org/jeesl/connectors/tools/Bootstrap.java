package org.jeesl.connectors.tools;

import org.exlp.util.io.log.LoggerInit;

/**
 *
 * @author hhemm_000
 */
public class Bootstrap {
    
    /**
     *  Initialises the logging system for writing to console and file
     */
    public static void init()
    {
         LoggerInit loggerInit = new LoggerInit("log4j.xml");
			loggerInit.path("config");
			loggerInit.init();
    }
    
}
