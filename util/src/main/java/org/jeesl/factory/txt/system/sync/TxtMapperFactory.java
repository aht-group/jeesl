package org.jeesl.factory.txt.system.sync;

import org.jeesl.model.xml.io.ssi.sync.Mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtMapperFactory
{
	final static Logger logger = LoggerFactory.getLogger(TxtMapperFactory.class);
		
	public static String build(Mapper mapper)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("mapped id");
		
		sb.append(" [");
		try
		{
			Class<?> c = Class.forName(mapper.getClazz());
			sb.append(c.getSimpleName());
		}
		catch (ClassNotFoundException e)
		{
			sb.append("--UNKNOWN--");
		}
		sb.append("]");
		
		sb.append(" ").append(mapper.getOldId());
		sb.append("->");
		sb.append(mapper.getNewId());
		return sb.toString();
		
	}
}