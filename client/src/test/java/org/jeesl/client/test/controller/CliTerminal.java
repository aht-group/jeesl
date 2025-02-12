package org.jeesl.client.test.controller;

import org.jeesl.client.app.JeeslBootstrap;
import org.jeesl.controller.handler.system.io.ssi.SsiTerminalHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.test.AbstractJeeslTest;

public class CliTerminal extends AbstractJeeslTest
{
	final static Logger logger = LoggerFactory.getLogger(CliTerminal.class);

    public static void main (String[] args) throws Exception
	{
    	JeeslBootstrap.init();
    	SsiTerminalHandler terminal = new SsiTerminalHandler();
    	terminal.shell();
    	String s = terminal.command("tail -f /Volumes/ramdisk/jboss/standalone/log/gc.log.0.current");
    	logger.info(s);
     }
}