package org.jeesl.factory.txt.io.ssi.core;

import java.util.Objects;

import org.jeesl.factory.builder.AbstractFactoryBuilder;
import org.jeesl.model.json.io.ssi.core.JsonSsiCredential;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtIoSsiCredentialFactory
{
	private final static Logger logger = LoggerFactory.getLogger(AbstractFactoryBuilder.class);
	
	private TxtIoSsiCredentialFactory()
	{
		logger.trace("Instantiated");
	}
	
	public static String toJdbc(JsonSsiCredential json)
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("jdbc:").append(json.getSystem().getCode()).append("://");
		sb.append(json.getHost());
		sb.append(":").append(json.getPort());
		sb.append("/").append(json.getToken());
		if(Objects.nonNull(json.getUrl())) {sb.append(json.getUrl());}
		
//		logger.info(sb.toString());
		return sb.toString();
	}
}