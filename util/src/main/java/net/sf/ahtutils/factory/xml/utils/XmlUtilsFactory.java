package net.sf.ahtutils.factory.xml.utils;

import org.jeesl.model.xml.system.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlUtilsFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlUtilsFactory.class);
		
	public static Utils build()
	{
		Utils xml = new Utils();
		return xml;
	}
}