package org.jeesl.controller.io.mail.smtp;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jeesl.controller.io.mail.freemarker.FreemarkerEngine;
import org.jeesl.controller.io.mail.msg.FreemarkerMimeContentCreator;
import org.jeesl.controller.io.mail.msg.MimeMessageCreator;
import org.jeesl.exception.processing.UtilsMailException;
import org.jeesl.exception.processing.UtilsProcessingException;
import org.jeesl.model.xml.io.mail.Bcc;
import org.jeesl.model.xml.io.mail.Header;
import org.jeesl.model.xml.io.mail.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.xml.JDomUtil;

public class TemplateMailSender extends AbstractMailSender
{
	final static Logger logger = LoggerFactory.getLogger(TemplateMailSender.class);
	
	private final FreemarkerEngine fme;
	
	public TemplateMailSender(String smtpHost,FreemarkerEngine fme) {this(smtpHost,25,fme);}
	public TemplateMailSender(String smtpHost, int smtpPort,FreemarkerEngine fme)
	{
		super(smtpHost,smtpPort);
		this.fme=fme;
	}

	@Deprecated
	public void send(Document doc) throws UnsupportedEncodingException, MessagingException, UtilsProcessingException, UtilsMailException
	{
		buildSession();
		MimeMessage message = new MimeMessage(session);
		MimeMessageCreator mmc = new MimeMessageCreator(message);
		
		Header header = getHeader(doc.getRootElement());
		if(overrideOnlyTo!=null)
		{
			header.setBcc(null);
			header.setCc(null);
			header.getTo().getEmailAddress().clear();
			header.getTo().getEmailAddress().add(overrideOnlyTo);
		}
		else
		{
			if(Objects.isNull(header.getBcc())) {header.setBcc(new Bcc());}
			header.getBcc().getEmailAddress().addAll(alwaysBcc);
		}
		mmc.createHeader(header);
		
		Mail mail = getMailAndDetachAtt(doc.getRootElement());
		
		if(Objects.isNull(mail.getLang()))
		{
			mail.setLang("de");
			logger.warn("No @lang is set in this mail! Setting to "+mail.getLang());
		}
		
		FreemarkerMimeContentCreator mcc = new FreemarkerMimeContentCreator(message, fme);
		mcc.createContent(doc,mail);
		
		connect();
		logger.trace("SENDING ...");
		transport.sendMessage(message, message.getAllRecipients());
		logger.trace("SENT");
	}
	
	private Header getHeader(Element root) throws UtilsProcessingException
	{
		logger.trace("Parsing header");
		for(Object o: root.getContent())
		{
			Element e = (Element)o;
			if(e.getName().equals("header"))
			{
				logger.warn("This should be avaoided, see UTILS-200");
				return JDomUtil.toJaxb(e, Header.class);
			}
		}
		Element mail = root.getChild("mail", nsMail);
		if(mail!=null)
		{
			Element header = mail.getChild("header", nsMail);
			if(header!=null){return JDomUtil.toJaxb(header, Header.class);}
		}
		logger.info(mail.toString());
		throw new UtilsProcessingException("No <header> (or <mail><header/></mail>) Element found");
	}
	
	private Mail getMailAndDetachAtt(Element root) throws UtilsProcessingException
	{
		logger.trace("Parsing Mail");
		for(Content content: root.getContent())
		{
			Element e = (Element)content;
			if(e.getName().equals("mail"))
			{
//				for(Element att : e.getChildren("attachment", nsMail))
				{
		//			att.detach();
				}
				return JDomUtil.toJaxb(e, Mail.class);
			}
		}
		throw new UtilsProcessingException("No <mail> Element found");
	}
}