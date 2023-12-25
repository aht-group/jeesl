package org.jeesl.factory.xml.dev.qa;

import net.sf.ahtutils.xml.qa.Steps;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlStepsFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlStepsFactory.class);
	
	public static Steps build(String value)
	{
		Steps xml = new Steps();
		xml.setValue(value);
		return xml;
	}
}