package org.jeesl.factory.xml.dev.qa;

import org.jeesl.model.xml.module.dev.qa.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlReferenceFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlReferenceFactory.class);
	
	public static Reference build(String value)
	{
		Reference xml = new Reference();
		xml.setValue(value);
		return xml;
	}
}