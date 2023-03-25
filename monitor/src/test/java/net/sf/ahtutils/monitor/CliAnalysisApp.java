package net.sf.ahtutils.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.test.UtilsMonitorTestBootstrap;

public class CliAnalysisApp
{
	final static Logger logger = LoggerFactory.getLogger(CliAnalysisApp.class);
	
	public static void main (String[] args)
	{
		UtilsMonitorTestBootstrap.init();
		
	    new IndicatorWorker();

	  }
}