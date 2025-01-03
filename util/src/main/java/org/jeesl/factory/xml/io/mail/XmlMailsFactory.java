package org.jeesl.factory.xml.io.mail;

import java.util.List;

import org.jeesl.model.xml.io.mail.Mail;
import org.jeesl.model.xml.io.mail.Mails;
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
	public static Mails build(List<Mail> list)
	{
		Mails xml = XmlMailsFactory.build();
		xml.getMail().addAll(list);
		return xml;
	}
}