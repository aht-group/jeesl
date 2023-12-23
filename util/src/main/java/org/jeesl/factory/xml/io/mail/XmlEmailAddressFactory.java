package org.jeesl.factory.xml.io.mail;

import javax.mail.Address;
import javax.mail.internet.InternetAddress;

import org.jeesl.interfaces.model.system.security.user.JeeslSimpleUser;
import org.jeesl.model.xml.io.mail.EmailAddress;
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
    
    public static EmailAddress build(JeeslSimpleUser user)
    {
    	StringBuilder sb = new StringBuilder();
    	sb.append(user.getFirstName());
    	sb.append(" ").append(user.getLastName());
    	return create(sb.toString(),user.getEmail());
    }
}