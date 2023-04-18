package org.jeesl.mail.smtp;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jeesl.controller.mail.freemarker.FreemarkerEngine;
import org.jeesl.exception.processing.UtilsMailException;
import org.jeesl.exception.processing.UtilsProcessingException;
import org.jeesl.mail.msg.FreemarkerMimeContentCreator;
import org.jeesl.mail.msg.MimeMessageCreator;
import org.jeesl.mail.msg.XmlMimeContentCreator;
import org.jeesl.model.xml.system.io.mail.Bcc;
import org.jeesl.model.xml.system.io.mail.Header;
import org.jeesl.model.xml.system.io.mail.Mail;
import org.jeesl.model.xml.system.io.mail.Mails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.xml.JDomUtil;

public class XmlMailSender extends AbstractMailSender
{
	final static Logger logger = LoggerFactory.getLogger(XmlMailSender.class);
	
	private FreemarkerEngine fme;
	
	public XmlMailSender(String smtpHost) {this(null,smtpHost);}
	public XmlMailSender(FreemarkerEngine fme, String smtpHost){this(fme,smtpHost,25);}
	public XmlMailSender(FreemarkerEngine fme, String smtpHost, int smtpPort)
	{
		super(smtpHost,smtpPort);
		this.fme=fme;
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
	
	@Deprecated
	public void send(FreemarkerEngine fme, String lang, Document doc) throws UnsupportedEncodingException, MessagingException, UtilsProcessingException, UtilsMailException
	{
		this.fme=fme;
		send(lang, doc);
	}
	
	@Deprecated
	public void send(String lang, Document doc) throws UnsupportedEncodingException, MessagingException, UtilsProcessingException, UtilsMailException
	{
		send(doc,lang);
	}
	public void send(Document doc) throws UnsupportedEncodingException, MessagingException, UtilsProcessingException, UtilsMailException
	{
		send(doc,null);
	}
	
	private void send(Document doc, String lang) throws UnsupportedEncodingException, MessagingException, UtilsProcessingException, UtilsMailException
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
			if(!header.isSetBcc()){header.setBcc(new Bcc());}
			header.getBcc().getEmailAddress().addAll(alwaysBcc);
		}
		mmc.createHeader(header);
		
		Mail mail = getMailAndDetachAtt(doc.getRootElement());
		
		if(!mail.isSetLang())
		{
			if(lang!=null){mail.setLang(lang);}
			else{mail.setLang("de");}
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