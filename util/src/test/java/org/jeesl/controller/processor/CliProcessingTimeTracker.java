package org.jeesl.controller.processor;

import org.jeesl.controller.monitoring.counter.ProcessingTimeTracker;
import org.jeesl.test.JeeslBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CliProcessingTimeTracker
{
	final static Logger logger = LoggerFactory.getLogger(CliProcessingTimeTracker.class);
	
	public CliProcessingTimeTracker()
	{
		
	}
	
	private void basic() throws InterruptedException
	{
		ProcessingTimeTracker ptt = ProcessingTimeTracker.instance().start();
		Thread.sleep(2000);
		ptt.stop();
		
		logger.info(ptt.toTotalPeriod());
	}
	
	public static void main(String[] args) throws Exception
	{
		JeeslBootstrap.init();
		CliProcessingTimeTracker cli = new CliProcessingTimeTracker();
		
		cli.basic();
	}
}