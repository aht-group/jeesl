package org.jeesl.factory.xml.system.io.sync;


import java.util.Date;
import java.util.Objects;

import org.exlp.util.system.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlExceptionFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlExceptionFactory.class);
			
	public static org.jeesl.model.xml.io.ssi.sync.Exception build(Throwable e)
	{
		org.jeesl.model.xml.io.ssi.sync.Exception xml = new org.jeesl.model.xml.io.ssi.sync.Exception();
		xml.setType(e.getClass().getName());
		xml.setMessage(e.getMessage());
		xml.setRecord(DateUtil.toXmlGc(new Date()));
		
		if(e.getStackTrace() != null && e.getStackTrace().length>0)
		{
			StackTraceElement st = e.getStackTrace()[0];
			xml.setClassName(st.getClassName());
			xml.setLine(st.getLineNumber());
		}
		
		if(e.getCause() != null)
		{
			xml.setException(XmlExceptionFactory.build(e.getCause()));
		}
		
		if(Objects.nonNull(xml.getException())) {removeRecursiveDates(xml.getException());}
		
		return xml;
	}
	
	private static void removeRecursiveDates(org.jeesl.model.xml.io.ssi.sync.Exception xml)
	{
		xml.setRecord(null);
		if(Objects.nonNull(xml.getException())) {removeRecursiveDates(xml.getException());}
	}
}