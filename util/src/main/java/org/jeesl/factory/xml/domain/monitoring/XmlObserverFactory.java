package org.jeesl.factory.xml.domain.monitoring;

import org.jeesl.model.xml.module.monitoring.Observer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlObserverFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlObserverFactory.class);
	
	public static Observer build(String code)
	{
		Observer xml = new Observer();		
		xml.setCode(code);
		
		return xml;
	}
}