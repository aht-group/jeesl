package org.jeesl.factory.xml.io.locale.status;

import org.jeesl.model.xml.io.locale.status.Parent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlParentFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlParentFactory.class);
	
	public static Parent create(String code)
	{
		Parent xml = new Parent();
		xml.setCode(code);
		return xml;
	}
}