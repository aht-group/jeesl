package org.jeesl.factory.xml.io.mail;

import org.jeesl.model.xml.io.mail.Html;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlHtmlFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlHtmlFactory.class);
	
	public static Html build(String content)
	{
		Html xml = new Html();
		xml.setValue(content);
		return xml;
	}
}