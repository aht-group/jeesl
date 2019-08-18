package org.jeesl.controller.facade.lookup;

import java.util.HashMap;
import java.util.Map;

import javax.naming.NamingException;

import org.jeesl.api.facade.JeeslFacadeLookup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JeeslFacadeCache implements JeeslFacadeLookup
{
	final static Logger logger = LoggerFactory.getLogger(JeeslFacadeCache.class);
	
	private JeeslFacadeLookup lookup;
	private Map<Class<?>,Object> map;
	
	public JeeslFacadeCache(JeeslFacadeLookup lookup)
	{
		this.lookup=lookup;
	}
	
	public JeeslFacadeCache()
	{
		map = new HashMap<Class<?>,Object>();
	}
	
	public <F> void add(Class<?> c, F facade)
	{
		map.put(c,facade);
	}

	@SuppressWarnings("unchecked")
	@Override public <F> F lookup(Class<F> facade) throws NamingException
	{
		if(lookup!=null) {return lookup.lookup(facade);}
		else
		{
			if(map.containsKey(facade))
			{
				return (F)map.get(facade);
			}
			else
			{
				logger.info("In Cache:");
				for(Class<?> c : map.keySet())
				{
					logger.info(c.getName());
				}
				throw new NamingException("Not in cache "+facade.getName());
			}
		}
	}

	
}