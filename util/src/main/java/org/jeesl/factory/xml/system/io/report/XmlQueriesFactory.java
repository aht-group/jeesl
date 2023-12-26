package org.jeesl.factory.xml.system.io.report;

import org.jeesl.model.xml.io.report.Queries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlQueriesFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlQueriesFactory.class);
	
	public static Queries build()
	{
		Queries xml = new Queries();

		return xml;
	}
}