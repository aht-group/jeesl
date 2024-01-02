package org.jeesl.controller.io.mail.smtp;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jeesl.controller.io.mail.freemarker.FreemarkerEngine;
import org.jeesl.controller.io.mail.msg.FreemarkerMimeContentCreator;
import org.jeesl.controller.io.mail.msg.MimeMessageCreator;
import org.jeesl.controller.io.mail.msg.XmlMimeContentCreator;
import org.jeesl.exception.processing.UtilsMailException;
import org.jeesl.exception.processing.UtilsProcessingException;
import org.jeesl.model.xml.io.mail.Bcc;
import org.jeesl.model.xml.io.mail.Header;
import org.jeesl.model.xml.io.mail.Mail;
import org.jeesl.model.xml.io.mail.Mails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.xml.JDomUtil;


public class XmlMailSender extends AbstractMailSender
{
	final static Logger logger = LoggerFactory.getLogger(XmlMailSender.class);
	
	public XmlMailSender(String smtpHost) {this(smtpHost,25);}
	public XmlMailSender(String smtpHost, int smtpPort)
	{
		super(smtpHost,smtpPort);
	}

	public void send(Mails mails) throws MessagingException, UnsupportedEncodingException
	{
		for(Mail mail : mails.getMail())
		{
			send(mail);
		}
	}
	
	public void send(Mail mail) throws MessagingException, UnsupportedEncodingException
	{
		buildSession();
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(mail.getHeader().getFrom().getEmailAddress().getEmail()));

		MimeMessageCreator mmc = new MimeMessageCreator(msg);
		mmc.createHeader(mail.getHeader());
				
		XmlMimeContentCreator mcc = new XmlMimeContentCreator(msg);
		mcc.buildContent(mail);
		
		Transport.send(msg);
	}
}