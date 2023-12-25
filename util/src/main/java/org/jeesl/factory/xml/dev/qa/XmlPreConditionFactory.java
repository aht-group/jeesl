package org.jeesl.factory.xml.dev.qa;

import net.sf.ahtutils.xml.qa.PreCondition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlPreConditionFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlPreConditionFactory.class);
	
	public static PreCondition build(String value)
	{
		PreCondition xml = new PreCondition();
		xml.setValue(value);
		return xml;
	}
}