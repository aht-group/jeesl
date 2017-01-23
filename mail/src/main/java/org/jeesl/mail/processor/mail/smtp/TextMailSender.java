package org.jeesl.mail.processor.mail.smtp;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.jeesl.mail.AbstractMailSender;
import org.jeesl.mail.MimeMessageCreator;
import org.jeesl.mail.processor.mail.msg.XmlMimeContentCreator;
import org.jeesl.model.xml.system.io.mail.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextMailSender extends AbstractMailSender
{
	final static Logger logger = LoggerFactory.getLogger(TextMailSender.class);
	
	public TextMailSender(String smtpHost){this(smtpHost,25);}
	public TextMailSender(String smtpHost, int smtpPort)
	{
		super(smtpHost,smtpPort);
	}

	public void send(Mail mail) throws MessagingException
	{
		buildSession();
		
		MimeMessage msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(mail.getHeader().getFrom().getEmailAddress().getEmail()));

		MimeMessageCreator mmc = new MimeMessageCreator(msg);
		try
		{
			mmc.createHeader(mail.getHeader());
		}
		catch (UnsupportedEncodingException e) {e.printStackTrace();}
				
		XmlMimeContentCreator mcc = new XmlMimeContentCreator(msg);
		mcc.buildContent(mail);
		
		connect();
		logger.debug("SENDING");
		transport.sendMessage(msg,msg.getAllRecipients());
		logger.debug("SENT");
	}
}