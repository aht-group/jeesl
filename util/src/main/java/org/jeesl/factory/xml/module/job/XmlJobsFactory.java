package org.jeesl.factory.xml.module.job;

import org.jeesl.model.xml.module.job.Jobs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlJobsFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlJobsFactory.class);

	public static Jobs build()
	{
		Jobs xml = new Jobs();
		
		return xml;
	}
}