package org.jeesl.factory.xml.report;

import org.jeesl.model.xml.io.report.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlUserFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlUserFactory.class);
		
	public static User create(String user)
	{
		User xml = new User();
		xml.setValue(user);
		return xml;
		
	}
}