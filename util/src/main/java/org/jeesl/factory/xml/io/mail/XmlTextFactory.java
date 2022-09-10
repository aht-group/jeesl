package org.jeesl.factory.xml.io.mail;

import org.jeesl.model.xml.system.io.mail.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlTextFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlTextFactory.class);
	
	public static Text build(String text)
	{
		Text xml = new Text();
		xml.setValue(text);
		return xml;
	}
}