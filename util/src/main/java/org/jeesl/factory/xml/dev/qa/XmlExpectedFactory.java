package org.jeesl.factory.xml.dev.qa;

import org.jeesl.model.xml.module.dev.qa.Expected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlExpectedFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlExpectedFactory.class);
	
	public static Expected build(String value)
	{
		Expected xml = new Expected();
		xml.setValue(value);
		return xml;
	}
}