package org.jeesl.controller.handler.rest;

import java.time.LocalDateTime;

import org.jeesl.api.rest.i.system.JeeslTestRestInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestRestHandler implements JeeslTestRestInterface
{
	final static Logger logger = LoggerFactory.getLogger(TestRestHandler.class);

	public TestRestHandler()
	{
		
	}

	@Override public String dateTimePublic()
	{
		String txt = LocalDateTime.now().toString();
		logger.info("datePublic:"+txt);
		return txt;
	}
	
	@Override public String dateTimeRestricted()
	{
		String txt = LocalDateTime.now().toString();
		logger.info("datePublic:"+txt);
		return txt;
	}
}