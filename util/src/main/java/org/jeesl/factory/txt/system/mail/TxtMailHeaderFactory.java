package org.jeesl.factory.txt.system.mail;

import org.jeesl.model.xml.system.io.mail.EmailAddress;
import org.jeesl.model.xml.system.io.mail.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtMailHeaderFactory
{
	final static Logger logger = LoggerFactory.getLogger(TxtMailHeaderFactory.class);
		
	public static String to(Header header)
	{
		StringBuffer sb = new StringBuffer();
		if(header.isSetTo())
		{
			for(EmailAddress email : header.getTo().getEmailAddress())
			{
				sb.append(email.getEmail());
				sb.append(", ");
			}
		}
		
		else
		{
			sb.append("No TO field");
		}
		return sb.toString();
	}
}