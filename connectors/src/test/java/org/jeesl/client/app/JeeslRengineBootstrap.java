package org.jeesl.client.app;

import org.exlp.controller.handler.io.log.LoggerBootstrap;

public class JeeslRengineBootstrap
{
    
    /**
     *  Initialises the logging system for writing to console and file
     */
    public static void init()
    {
    	LoggerBootstrap.instance().path("jeesl/system/io/log").init();
    }
    
}
