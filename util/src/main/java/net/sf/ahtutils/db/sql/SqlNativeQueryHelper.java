package net.sf.ahtutils.db.sql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlNativeQueryHelper
{
	final static Logger logger = LoggerFactory.getLogger(SqlNativeQueryHelper.class);
	
	
	public static void debugDataTypes(Object[] array)
	{
		logger.info("*****************************");
		int i=0;
		for(Object o : array)
		{
			if(o!=null)
			{
				logger.info(i+" "+o.getClass().getName());
			}
			else
			{
				logger.info(i+" NULL");
			}
			i++;
		}
	}
	
	public static boolean withoutNullObjects(Object[] array)
	{
		for(Object o : array)
		{
			if(o==null){return false;}
		}
		return true;
	}
	
	public static String differenceInDays(String from, String to, String s)
	{
		String age = "age("+to+s+s+"timestamp"+s+s+"date, "+from+s+s+"timestamp"+s+s+"date)";
		
		StringBuffer sb = new StringBuffer();
		sb.append("EXTRACT(epoch FROM "+age+")/(3600*24)");
		return sb.toString();
	}
	
	public static String semicolon(boolean escape)
	{
		if(escape){return "\\:";}
		else{return ":";}
	}
	public static String semicolon2(boolean escape)
	{
		if(escape){return "\\:\\:";}
		else{return "::";}
	}
}