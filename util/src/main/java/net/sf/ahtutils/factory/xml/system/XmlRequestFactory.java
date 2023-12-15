package net.sf.ahtutils.factory.xml.system;

import net.sf.ahtutils.xml.system.Request;

import org.jeesl.factory.xml.system.status.XmlTypeFactory;
import org.jeesl.model.xml.io.locale.status.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlRequestFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlRequestFactory.class);
		
	public static Request build(String type, long counter)
	{
		Type xmlType = XmlTypeFactory.create(type);
		return build(xmlType, counter);
	}
	
	public static Request build(Type type, long counter)
	{
		Request xml = new Request();
		xml.setType(type);
		xml.setCounter(counter);
		return xml;
	}
}