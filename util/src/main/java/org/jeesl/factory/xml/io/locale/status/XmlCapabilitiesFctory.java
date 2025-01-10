package org.jeesl.factory.xml.io.locale.status;

import org.jeesl.model.xml.io.locale.status.Capabilities;
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