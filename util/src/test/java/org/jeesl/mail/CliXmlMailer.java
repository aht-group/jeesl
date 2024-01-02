package org.jeesl.mail;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Random;

import javax.activation.MimetypesFileTypeMap;
import javax.mail.MessagingException;

import org.apache.commons.configuration.Configuration;
import org.jeesl.JeeslBootstrap;
import org.jeesl.controller.io.mail.freemarker.FreemarkerEngine;
import org.jeesl.controller.io.mail.smtp.XmlMailSender;
import org.jeesl.exception.processing.UtilsMailException;
import org.jeesl.exception.processing.UtilsProcessingException;
import org.jeesl.factory.xml.io.mail.XmlAttachmentFactory;
import org.jeesl.factory.xml.io.mail.XmlHeaderFactory;
import org.jeesl.factory.xml.io.mail.XmlMailFactory;
import org.jeesl.factory.xml.system.util.text.XmlExampleFactory;
import org.jeesl.model.xml.io.mail.Header;
import org.jeesl.model.xml.io.mail.Mail;
import org.jeesl.model.xml.io.mail.Mails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.interfaces.util.ConfigKey;
import net.sf.exlp.util.xml.JaxbUtil;

public class CliXmlMailer
{
	final static Logger logger = LoggerFactory.getLogger(CliXmlMailer.class);
	
	private XmlMailSender xmlMailer;
	
	private Configuration config;
	private String from,to;
	
	public CliXmlMailer(Configuration config) throws FileNotFoundException
	{
		this.config=config;
		
		Mails xmlMailsDefinition = JaxbUtil.loadJAXB("mails.ahtutils-mail.test/mails.xml", Mails.class);
		FreemarkerEngine fme = new FreemarkerEngine(xmlMailsDefinition);
		
		xmlMailer = new XmlMailSender(fme,config.getString(ConfigKey.netSmtpHost));
		from = config.getString("net.smtp.test.from");
		to = config.getString("net.smtp.test.to");
	}
	
	public void tlsAuthenticate()
	{
		xmlMailer.tlsPasswordAuthentication(config.getString(ConfigKey.netSmtpUser.toString()), config.getString(ConfigKey.netSmtpPwd.toString()));
	}
	
	public void sendMsg(int i) throws MessagingException, UnsupportedEncodingException, UtilsProcessingException, UtilsMailException
	{
		Mail container = new Mail();
		
		Header header = XmlHeaderFactory.create(CliXmlMailer.class.getSimpleName()+" "+i, from, to);
		Mail mail = XmlMailFactory.build(header, "test only");
		mail.setLang("de");
		mail.setCode("test");
		
		Mail mailContent = new Mail();
		mailContent.setExample(XmlExampleFactory.build("myExample"));
		mail.setMail(mailContent);
		
		byte[] data = new byte[10];
		Random rnd = new Random();
		rnd.nextBytes(data);
		
		String fName = "x.pdf";
		String mimeType = MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(fName);
		
		mail.getAttachment().add(XmlAttachmentFactory.create(fName, mimeType, data));
		
		container.setMail(mail);
		xmlMailer.send(JaxbUtil.toDocument(container));
	}
	
	public static void main (String[] args) throws Exception
	{
		Configuration config = JeeslBootstrap.init();
		
		CliXmlMailer cli = new CliXmlMailer(config);
		cli.tlsAuthenticate();
		cli.sendMsg(1);
	}
}