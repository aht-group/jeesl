package org.jeesl.controller.handler.rest;

import java.time.LocalDateTime;

import org.jeesl.api.rest.i.system.JeeslTestRestInterface;
import org.jeesl.model.json.io.ssi.update.JsonSsiUpdate;
import org.jeesl.model.json.system.job.JsonSystemJob;
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

	@Override
	public JsonSsiUpdate jsonUpdate()
	{
		JsonSsiUpdate json = new JsonSsiUpdate();
		json.setSuccess(true);
		return json;
	}

	@Override
	public JsonSystemJob jsonJob()
	{
		JsonSystemJob json = new JsonSystemJob();
		json.setStart(LocalDateTime.now());
		return json;
	}

	@Override public String jsonMirror(String content)
	{
		logger.info(content);
		return content;
	}
}