package org.jeesl.factory.xml.module.inventory.pc;

import org.jeesl.model.xml.module.inventory.pc.Updates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlUpdatesFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlUpdatesFactory.class);
	
	public static Updates build()
	{
		Updates xml = new Updates();

		return xml;
	}
}