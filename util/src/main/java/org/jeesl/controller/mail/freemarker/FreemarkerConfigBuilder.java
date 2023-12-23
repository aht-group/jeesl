package org.jeesl.controller.mail.freemarker;

import org.jeesl.factory.xml.io.mail.XmlMailFactory;
import org.jeesl.model.xml.io.mail.Mail;
import org.jeesl.model.xml.io.mail.Mails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FreemarkerConfigBuilder
{
	final static Logger logger = LoggerFactory.getLogger(FreemarkerConfigBuilder.class);

	@SuppressWarnings("unused")
	private Mails mails;
	
	public FreemarkerConfigBuilder(Mails mails)
	{
		this.mails=mails;
	}
	
	public Mail build(String id, String lang, String type)
	{
		Mail mail = XmlMailFactory.create(id, lang, type);
		return mail;
	}
}