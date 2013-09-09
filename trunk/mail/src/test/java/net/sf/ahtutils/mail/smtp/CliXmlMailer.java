package net.sf.ahtutils.mail.smtp;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Random;

import javax.activation.MimetypesFileTypeMap;
import javax.mail.MessagingException;

import net.sf.ahtutils.exception.processing.UtilsMailException;
import net.sf.ahtutils.exception.processing.UtilsProcessingException;
import net.sf.ahtutils.factory.xml.mail.XmlAttachmentFactory;
import net.sf.ahtutils.factory.xml.mail.XmlHeaderFactory;
import net.sf.ahtutils.factory.xml.mail.XmlMailFactory;
import net.sf.ahtutils.mail.XmlMailSender;
import net.sf.ahtutils.mail.freemarker.FreemarkerEngine;
import net.sf.ahtutils.test.UtilsMailTestBootstrap;
import net.sf.ahtutils.xml.mail.Header;
import net.sf.ahtutils.xml.mail.Mail;
import net.sf.ahtutils.xml.mail.Mails;
import net.sf.exlp.interfaces.util.ConfigKey;
import net.sf.exlp.util.xml.JaxbUtil;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		Mail mail = XmlMailFactory.create(header, "test only");
		mail.setLang("de");
		mail.setCode("test");
		
		Mail mailContent = new Mail();
		mailContent.setExample("mYTest");
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
		Configuration config = UtilsMailTestBootstrap.init();
		
		CliXmlMailer cli = new CliXmlMailer(config);
		cli.tlsAuthenticate();
		cli.sendMsg(1);
	}
}