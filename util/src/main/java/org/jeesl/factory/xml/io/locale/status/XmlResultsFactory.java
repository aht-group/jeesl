package org.jeesl.factory.xml.io.locale.status;

import org.jeesl.model.xml.io.locale.status.Result;
import org.jeesl.model.xml.io.locale.status.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlResultsFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlResultsFactory.class);
		
	public static Results build()
	{
		Results xml = new Results();

		return xml;
	}
	
	public static Results build(Result result)
	{
		Results xml = new Results();
		xml.getResult().add(result);
		return xml;
	}
}