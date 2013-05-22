package net.sf.ahtutils.controller.factory.xml.mail;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;

import net.sf.ahtutils.xml.mail.EmailAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlEmailAddressFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlEmailAddressFactory.class);
		
	public XmlEmailAddressFactory()
	{
		
	}
	
	public static EmailAddress create(String email){return create(null, email);}
    public static EmailAddress create(String name, String email)
    {
    	EmailAddress xml = new EmailAddress();
    	xml.setName(name);
    	xml.setEmail(email);
    	return xml;
    }
    
    public EmailAddress build(Address address){return build((InternetAddress)address);}
    public EmailAddress build(InternetAddress address)
    {
    	EmailAddress xml = new EmailAddress();
    	xml.setName(address.getPersonal());
    	xml.setEmail(address.getAddress());
    	return xml;
    }
}