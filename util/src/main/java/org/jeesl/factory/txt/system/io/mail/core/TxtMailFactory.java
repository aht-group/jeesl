package org.jeesl.factory.txt.system.io.mail.core;

import java.io.IOException;
import java.io.OutputStream;

import org.jeesl.model.xml.io.mail.Mail;
import org.jeesl.model.xml.io.mail.Mails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtMailFactory
{
	final static Logger logger = LoggerFactory.getLogger(TxtMailFactory.class);
		
	public static String debug(Mail mail)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(mail.getHeader().getSubject());
		sb.append(" -> ").append(mail.getHeader().getTo().getEmailAddress().get(0).getEmail());
		return sb.toString();
	}
	
	public static void debug(Mails mails, OutputStream os)
	{
		logger.info("Debugging "+Mails.class.getSimpleName()+" "+mails.getMail().size());
		for(Mail mail : mails.getMail())
		{
			try {
				os.write(debug(mail).getBytes());
				os.write(System.lineSeparator().getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}