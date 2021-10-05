package org.jeesl.factory.xml.system.io.mail;

import org.jeesl.model.xml.system.io.mail.Cc;
import org.jeesl.model.xml.system.io.mail.EmailAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlCcFactory
{
	final static Logger logger = LoggerFactory.getLogger(XmlEmailAddressFactory.class);
    
    public static Cc build()
    {    	
    	return new Cc();
    }
    
    public static Cc build(EmailAddress... addresses)
    {
    	Cc cc = build();
    	for(EmailAddress a : addresses) {cc.getEmailAddress().add(a);}
    	return cc;
    }
}