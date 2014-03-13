package net.sf.ahtutils.factory.xml.status;

import net.sf.ahtutils.xml.status.Parent;

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