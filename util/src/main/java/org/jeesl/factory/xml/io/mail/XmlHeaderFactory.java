package org.jeesl.factory.xml.io.mail;

import javax.mail.Message;
import javax.mail.MessagingException;

import org.jeesl.model.xml.io.mail.EmailAddress;
import org.jeesl.model.xml.io.mail.From;
import org.jeesl.model.xml.io.mail.Header;
import org.jeesl.model.xml.io.mail.To;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlHeaderFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlEmailAddressFactory.class);
	
	public XmlHeaderFactory()
	{
		
	}
	
    public static Header create(String subject, String from, String to)
    {
    	return build(subject,XmlEmailAddressFactory.create(from),XmlEmailAddressFactory.create(to));
    }
    
    public static Header build(String subject, EmailAddress from, EmailAddress to)
    {
    	Header xml = new Header();
    	xml.setSubject(subject);
    	
    	From xmlFrom = new From();
    	xmlFrom.setEmailAddress(from);
    	xml.setFrom(xmlFrom);
    	
    	To xmlTo = new To();
    	xmlTo.getEmailAddress().add(to);
    	xml.setTo(xmlTo);
    	
    	return xml;
    }
    
    public Header build(Message message) throws MessagingException
    {
    	Header xml = new Header();
    	xml.setSubject(message.getSubject());
    	
    	XmlFromFactory f = new XmlFromFactory();
    	xml.setFrom(f.build(message));
    	
    	return xml;
    }
}