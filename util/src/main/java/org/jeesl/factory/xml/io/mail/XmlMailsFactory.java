package org.jeesl.factory.xml.io.mail;

import org.jeesl.model.xml.system.io.mail.Mails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlMailsFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlMailsFactory.class);
	
	public static Mails build()
	{
		Mails xml = new Mails();
		return xml;
	}
}