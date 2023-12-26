package org.jeesl.factory.xml.domain.monitoring;

import org.jeesl.model.xml.module.monitoring.Indicator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlIndicatorFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlIndicatorFactory.class);
	
	public static Indicator build(String code)
	{
		Indicator xml = new Indicator();		
		xml.setCode(code);
		
		return xml;
	}
}