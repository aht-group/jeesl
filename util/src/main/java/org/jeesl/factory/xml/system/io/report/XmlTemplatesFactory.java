package org.jeesl.factory.xml.system.io.report;

import org.jeesl.model.xml.io.report.Templates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlTemplatesFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlTemplatesFactory.class);
	
	public static Templates build()
	{
		Templates xml = new Templates();
		return xml;
	}
}