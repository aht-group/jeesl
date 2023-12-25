package org.jeesl.factory.xml.dev.qa;

import net.sf.ahtutils.xml.qa.Description;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlDescriptionFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlDescriptionFactory.class);
	
	public static Description build(String value)
	{
		Description xml = new Description();
		xml.setValue(value);
		return xml;
	}
}