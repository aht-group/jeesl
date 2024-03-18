package org.jeesl.connectors.tools;

import org.exlp.controller.handler.io.log.LoggerBootstrap;

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
    	LoggerBootstrap.instance().path("jeesl/system/io/log").init();
    }
    
}
