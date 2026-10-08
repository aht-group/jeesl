package net.sf.ahtutils.factory.xml.aht;

import org.jeesl.model.xml.xsd.aht.Container;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlContainerFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlContainerFactory.class);
	
	public static Container build()
	{
		Container xml = new Container();
		return xml;
	}
}