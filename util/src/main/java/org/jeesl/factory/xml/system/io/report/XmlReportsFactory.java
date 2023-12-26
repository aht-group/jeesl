package org.jeesl.factory.xml.system.io.report;

import org.jeesl.model.xml.io.report.Reports;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlReportsFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlReportsFactory.class);
		
	public static Reports build()
	{
		Reports xml = new Reports();
		
		return xml;
	}
}