package org.jeesl.controller.handler.rest;

import java.time.LocalDateTime;
import java.util.Objects;

import org.jeesl.api.rest.i.system.JeeslTestRestInterface;
import org.jeesl.model.json.io.ssi.update.JsonSsiUpdate;
import org.jeesl.model.json.system.job.JsonSystemJob;
import org.jeesl.model.json.system.security.JsonSecurityUser;
import org.jeesl.model.json.util.JsonTime;
import org.jeesl.model.xml.test.Test;
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
		JsonSecurityUser user = new JsonSecurityUser();
		user.setFirstName("MyFirstName");
		user.setLastName("MyLastName");
		
		JsonSsiUpdate json = new JsonSsiUpdate();
		json.setSuccess(true);
		json.setUser(user);
		return json;
	}

	@Override
	public JsonSystemJob jsonJob()
	{
		JsonSystemJob json = new JsonSystemJob();
		json.setStart(LocalDateTime.now());
		return json;
	}

	@Override
	public JsonTime jsonTimeDownload()
	{
		JsonTime json = new JsonTime();
		json.setLdt1(LocalDateTime.now());
		json.setLdt2(LocalDateTime.now());
		
		return json;
	}
	
	@Override
	public JsonTime jsonTimeUpload(JsonTime json)
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("ldt1: ");
		if(Objects.nonNull(json.getLdt1()))
		{
			sb.append(json.getLdt1().toString());
			json.setLdt1(LocalDateTime.now());
		}
		else {sb.append("--");}
		logger.info(sb.toString());
		
		sb.setLength(0);
		sb.append("ldt2: ");
		if(Objects.nonNull(json.getLdt2()))
		{
			sb.append(json.getLdt2().toString());
			json.setLdt2(LocalDateTime.now());
		}
		else {sb.append("--");}
		logger.info(sb.toString());
		
		return json;
	}

	@Override public Test jaxb()
	{
		Test xml = new Test();
		xml.setKey("key");
		xml.setValue("value");
		return xml;
	}
}