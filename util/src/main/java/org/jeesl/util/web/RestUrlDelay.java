package org.jeesl.util.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.interfaces.util.ConfigKey;

public class RestUrlDelay
{
	final static Logger logger = LoggerFactory.getLogger(RestUrlDelay.class);
	
	public static String getUrl(org.apache.commons.configuration.Configuration config, String key, boolean delay)
	{
		if(delay) {return RestUrlDelay.getUrl(config,key);}
		else {return RestUrlDelay.getUrlNoDelay(config,key);}
	}
	
	public static String getUrlNoDelay(org.apache.commons.configuration.Configuration config, String key)
	{
		String url = config.getString(key);
		
		StringBuffer sb = new StringBuffer();
		sb.append("REST connection to ");
		sb.append(url);
		sb.append(" (").append(key).append(")");
		logger.info(sb.toString());
		return url;
	}
	
	public static String getUrl(org.apache.commons.configuration.Configuration config){return getUrl(config,ConfigKey.netRestUrlLocal);}
	public static String getUrl(org.apache.commons.configuration.Configuration config, String key)
	{
		String url = config.getString(key);
		
		StringBuffer sb = new StringBuffer();
		sb.append("REST connection to ");
		sb.append(url);
		sb.append(" (").append(key).append(")");
		
		boolean delay = false;
		if(!url.contains("localhost"))
		{
			sb.append("  .... delaying 5s");
			delay=true;
		}
		
		if(delay)
		{
			logger.warn(sb.toString());
			try {Thread.sleep(5000);}
			catch (InterruptedException e) {e.printStackTrace();}
		}
		else
		{
			logger.info(sb.toString());
		}
		
		return url;
	}
	
	
	public static String getUrl(org.apache.commons.configuration2.Configuration config, String key)
	{
		String url = config.getString(key);
		
		StringBuffer sb = new StringBuffer();
		sb.append("REST connection to ");
		sb.append(url);
		sb.append(" (").append(key).append(")");
		
		boolean delay = false;
		if(!url.contains("localhost"))
		{
			sb.append("  .... delaying 5s");
			delay=true;
		}
		
		if(delay)
		{
			logger.warn(sb.toString());
			try {Thread.sleep(5000);}
			catch (InterruptedException e) {e.printStackTrace();}
		}
		else
		{
			logger.info(sb.toString());
		}
		
		return url;
	}
}
