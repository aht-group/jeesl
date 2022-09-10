package org.jeesl.factory.xml.io.mail;

import javax.mail.Message;
import javax.mail.MessagingException;

import org.jeesl.factory.xml.system.util.text.XmlExampleFactory;
import org.jeesl.model.xml.system.io.mail.EmailAddress;
import org.jeesl.model.xml.system.io.mail.Header;
import org.jeesl.model.xml.system.io.mail.Mail;
import org.jeesl.model.xml.system.io.mail.Template;
import org.jeesl.model.xml.system.io.mail.To;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlMailFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlEmailAddressFactory.class);
	
	public static Mail build(String code)
	{
		Mail xml = new Mail();
		xml.setCode(code);
		return xml;
	}
	
    public static Mail create(String code, String lang, String type)
    {
    	Template template = new Template();
    	template.setLang(lang);
    	template.setType(type);
    	
    	Mail mail = new Mail();
    	mail.setCode(code);
    	mail.getTemplate().add(template);
    	
    	return mail;
    }
    
    public static Mail build(Header header, String text)
    {   	   	
    	Mail mail = new Mail();
    	mail.setHeader(header);
    	mail.setText(XmlTextFactory.build(text));
    	return mail;
    }
    public static Mail build(Header header)
    {   	   	
    	Mail mail = new Mail();
    	mail.setHeader(header);
    	
    	return mail;
    }
    
    public static Mail create(String subject, String emailFrom, String emailTo, String content)
    {    	
    	Mail mail = new Mail();
    	mail.setHeader(XmlHeaderFactory.create(subject, emailFrom, emailTo));
    	mail.setExample(XmlExampleFactory.build(content));
    	return mail;
    }
    
    public Mail build(Message message) throws MessagingException
    {
    	Mail mail = new Mail();
    	mail.setId(message.getMessageNumber());
    	mail.setMsgId(message.getHeader("Message-ID")[0]);
    	
    	XmlHeaderFactory f = new XmlHeaderFactory();
    	mail.setHeader(f.build(message));
    	
    	return mail;
    }
    
    public static void overwriteRecipients(Mail mail, EmailAddress address)
    {
    	if(mail.isSetHeader())
    	{
    		mail.getHeader().setBcc(null);
    		mail.getHeader().setTo(new To());
    		mail.getHeader().getTo().getEmailAddress().add(address);
    	}
    }
}