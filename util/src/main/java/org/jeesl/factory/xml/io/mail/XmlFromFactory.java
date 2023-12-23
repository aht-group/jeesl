package org.jeesl.factory.xml.io.mail;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;

import org.jeesl.model.xml.io.mail.From;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlFromFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlEmailAddressFactory.class);
	
    public static From create(String email)
    {    	
    	From xml = new From();
    	xml.setEmailAddress(XmlEmailAddressFactory.create(email));
    	
    	return xml;
    }
    
    public From build(Message message) throws MessagingException
    {
    	From xml = new From();
    	
    	XmlEmailAddressFactory f = new XmlEmailAddressFactory();
    	xml.setEmailAddress(f.build(message.getFrom()[0]));
    	
    	for(Address a : message.getFrom())
    	{
    		f.build(a);
    	}
    	
    	return xml;
    }
}