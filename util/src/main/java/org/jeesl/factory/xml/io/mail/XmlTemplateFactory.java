package org.jeesl.factory.xml.io.mail;

import org.jeesl.model.xml.system.io.mail.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlTemplateFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlTemplateFactory.class);
		
	public static Template build(String lang, String type)
	{
		Template xml = new Template();
		xml.setLang(lang);
		xml.setType(type);
		return xml;
	}
}