package net.sf.ahtutils.doc.resources;

import org.jeesl.test.JeeslDocBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CliUtilsIconTranscoder
{
	final static Logger logger = LoggerFactory.getLogger(CliUtilsIconTranscoder.class);


	public static void main(String args[]) throws Exception
	{
		JeeslDocBootstrap.init();
		logger.info(System.getProperty("java.version"));
	}
}
