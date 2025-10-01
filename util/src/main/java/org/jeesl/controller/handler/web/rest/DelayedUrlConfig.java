package org.jeesl.controller.handler.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DelayedUrlConfig
{
	final static Logger logger = LoggerFactory.getLogger(DelayedUrlConfig.class);
	
	public static String resolve(org.exlp.interfaces.system.property.Configuration config, String key) {return DelayedUrlConfig.resolve(false,config,key);}
	public static String resolve(boolean skipDelay, org.exlp.interfaces.system.property.Configuration config, String key)
	{
		String url = config.getString(key);

		StringBuffer sb = new StringBuffer();
		sb.append("REST connection to ");
		sb.append(url);
		sb.append(" (").append(key).append(")");
		
		boolean isLocal = url.contains("localhost");
//		sb.append(" skipDelay:").append(skipDelay);
//		sb.append(" localhost:").append(isLocal);
		
		if(!skipDelay && !isLocal)
		{
			sb.append("  .... delaying 5s");
			
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
	
	public static String resolve(org.apache.commons.configuration2.Configuration config, String key)
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