package org.jeesl.mail.smtp;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.jeesl.exception.processing.UtilsMailException;
import org.jeesl.mail.msg.FreemarkerMimeContentCreator;
import org.jeesl.mail.msg.MimeMessageCreator;
import org.jeesl.mail.msg.XmlMimeContentCreator;
import org.jeesl.mail.smtp.AbstractMailSender;
import org.jeesl.model.xml.system.io.mail.Bcc;
import org.jeesl.model.xml.system.io.mail.Header;
import org.jeesl.model.xml.system.io.mail.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HtmlMailSender extends AbstractMailSender
{
	final static Logger logger = LoggerFactory.getLogger(HtmlMailSender.class);
	
	public HtmlMailSender(String smtpHost){this(smtpHost,25);}
	private HtmlMailSender(String smtpHost, int smtpPort)
	{
		super(smtpHost,smtpPort);
	}

	public void send(Mail mail) throws MessagingException, UnsupportedEncodingException
	{
		logger.debug("buildSession");
		buildSession();
		
		MimeMessage message = new MimeMessage(session);
		MimeMessageCreator mmc = new MimeMessageCreator(message);
	
		mmc.createHeader(mail.getHeader());
		
		
		MimeBodyPart mbpTxt = new MimeBodyPart();
		mbpTxt.setContent(mail.getText().getValue(), "text/plain; charset=\""+FreemarkerMimeContentCreator.encoding+"\"");
				
		MimeBodyPart mbpHtml = new MimeBodyPart();
		mbpHtml.setContent(mail.getHtml().getValue(), "text/html; charset=\""+FreemarkerMimeContentCreator.encoding+"\"");
		
	
		Multipart mpAlternative = new MimeMultipart("alternative");
    	mpAlternative.addBodyPart(mbpTxt);
    	mpAlternative.addBodyPart(mbpHtml);
		
    	message.setContent(mpAlternative);
		
		connect();
		logger.debug("SENDING");
		transport.sendMessage(message,message.getAllRecipients());
		logger.debug("SENT");
	}
}