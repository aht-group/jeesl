package net.sf.ahtutils.factory.xml.status;

import net.sf.ahtutils.xml.status.Capabilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlCapabilitiesFctory
{
	final static Logger logger = LoggerFactory.getLogger(XmlCapabilitiesFctory.class);
	
	public static Capabilities build()
	{
		return new Capabilities();
	}
}