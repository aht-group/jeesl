package org.jeesl.util.web;

import java.io.IOException;
import java.util.Objects;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RestLogger implements ClientRequestFilter
{
	final static Logger logger = LoggerFactory.getLogger(RestLogger.class);
	
	@Override
    public void filter(ClientRequestContext ctx) throws IOException
	{
        logger.info(ctx.getMethod()+" "+ctx.getUri().toString());
        logger.info("Header:");
        for(String key : ctx.getStringHeaders().keySet())
        {        	
        	logger.info("\t"+key+": "+String.join(", ",ctx.getStringHeaders().get(key)));
        }

        if(ObjectUtils.isNotEmpty(ctx.getPropertyNames()))
        {
        	 logger.info("Properties:");
             for(String p : ctx.getPropertyNames())
             {
             	logger.info("\t"+p+": "+ctx.getProperty(p));
             }   
        }
        if(Objects.nonNull(ctx.getEntityClass())) {logger.info("Entity Class: "+ctx.getEntityClass());}
    }
}
