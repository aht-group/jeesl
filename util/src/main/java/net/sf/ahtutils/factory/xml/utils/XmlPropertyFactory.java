package net.sf.ahtutils.factory.xml.utils;

import org.jeesl.model.xml.system.util.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlPropertyFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlPropertyFactory.class);
		
	public static Property build(String code, Boolean value, boolean frozen)
	{	
		return build(code,value.toString(),frozen);
	}
	public static Property build(String code, String  value, boolean frozen)
	{
		Property xml = new Property();
		xml.setKey(code);
		xml.setValue(value);
		xml.setFrozen(frozen);
		return xml;
	}
}