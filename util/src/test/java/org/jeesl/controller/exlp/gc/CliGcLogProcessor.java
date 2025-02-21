package org.jeesl.controller.exlp.gc;

import org.exlp.interfaces.system.property.Configuration;
import org.jeesl.test.JeeslBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.core.handler.EhDebug;
import net.sf.exlp.core.listener.LogListenerTail;
import net.sf.exlp.interfaces.LogEventHandler;
import net.sf.exlp.interfaces.LogListener;
import net.sf.exlp.interfaces.LogParser;

public class CliGcLogProcessor
{
	final static Logger logger = LoggerFactory.getLogger(CliGcLogProcessor.class);

	private final Configuration config;

	public CliGcLogProcessor(Configuration config)
	{
		this.config=config;
	}

	public void jboss()
	{
		LogEventHandler leh = new EhDebug();
		LogParser lp = new WildflyGcParser(leh);
		
		LogListener ll = new LogListenerTail(lp,"/Volumes/ramdisk/jboss/standalone/log/gc.log.0.current");
		ll.processSingle();
	}

	public static void main(String[] args) throws Exception
	{
		Configuration config = JeeslBootstrap.init();
		
		CliGcLogProcessor cli = new CliGcLogProcessor(config);
		cli.jboss();
	}
}