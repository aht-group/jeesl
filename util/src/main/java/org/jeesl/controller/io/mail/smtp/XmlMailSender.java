package org.jeesl.controller.io.mail.smtp;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.jeesl.controller.io.mail.msg.MimeMessageCreator;
import org.jeesl.controller.io.mail.msg.XmlMimeContentCreator;
import org.jeesl.model.xml.io.mail.Mail;
import org.jeesl.model.xml.io.mail.Mails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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