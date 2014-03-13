package net.sf.ahtutils.factory.xml.monitor;

import net.sf.ahtutils.xml.monitoring.Value;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlValueFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlValueFactory.class);
	
	public static Value build(String value)
	{
		Value xml = new Value();		
		xml.setValue(value);
		return xml;
	}
	
	public static Value build(double value)
	{
		Value xml = new Value();		
		xml.setValue(""+value);
		return xml;
	}
}