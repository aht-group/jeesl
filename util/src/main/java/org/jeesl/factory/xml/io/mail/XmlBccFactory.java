package org.jeesl.factory.xml.io.mail;

import org.jeesl.model.xml.system.io.mail.Bcc;
import org.jeesl.model.xml.system.io.mail.EmailAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlBccFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlEmailAddressFactory.class);
    
    public static Bcc build()
    {    	
    	return new Bcc();
    }
    
    public static Bcc build(EmailAddress address)
    {
    	Bcc bcc = build();
    	bcc.getEmailAddress().add(address);
    	return bcc;
    }
}