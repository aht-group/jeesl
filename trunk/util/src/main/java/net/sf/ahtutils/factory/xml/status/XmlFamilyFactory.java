package net.sf.ahtutils.factory.xml.status;

import net.sf.ahtutils.xml.status.Family;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlFamilyFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlFamilyFactory.class);
		
	public static Family create(String code)
	{
		Family xml = new Family();
		xml.setCode(code);
		return xml;
	}
}