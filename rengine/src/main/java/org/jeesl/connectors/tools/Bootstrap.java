package org.jeesl.connectors.tools;

import net.sf.exlp.util.io.LoggerInit;

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
