package org.jeesl.factory.ejb.io.mail.core;

import java.util.Date;
import java.util.Objects;

import org.exlp.util.jx.JaxbUtil;
import org.jeesl.interfaces.model.io.mail.core.JeeslIoMail;
import org.jeesl.interfaces.model.io.mail.core.JeeslIoMailStatus;
import org.jeesl.interfaces.model.system.locale.status.JeeslStatus;
import org.jeesl.model.xml.io.mail.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EjbIoMailFactory <CATEGORY extends JeeslStatus<?,?,CATEGORY>,
								MAIL extends JeeslIoMail<?,?,CATEGORY,STATUS,RETENTION,?>,
								STATUS extends JeeslIoMailStatus<?,?,STATUS,?>,
								RETENTION extends JeeslStatus<?,?,RETENTION>>
{
	final static Logger logger = LoggerFactory.getLogger(EjbIoMailFactory.class);
	
	private final Class<MAIL> cMail;

	public EjbIoMailFactory(final Class<MAIL> cMail)
	{
        this.cMail = cMail;
	}
 
	public MAIL build(CATEGORY category, STATUS status, Mail mail, RETENTION retention)
	{
		MAIL ejb = null;
		try
		{
			ejb = cMail.newInstance();
			ejb.setCategory(category);
			ejb.setStatus(status);
			ejb.setRetention(retention);
			ejb.setCounter(0);
			ejb.setRecordCreation(new Date());
			if(Objects.nonNull(mail.getHeader()) && Objects.nonNull(mail.getHeader().getTo()) && Objects.nonNull(mail.getHeader().getTo().getEmailAddress())){ejb.setRecipient(mail.getHeader().getTo().getEmailAddress().get(0).getEmail());}
			ejb.setXml(JaxbUtil.toString(mail));
		}
		catch (InstantiationException e) {e.printStackTrace();}
		catch (IllegalAccessException e) {e.printStackTrace();}
		
		return ejb;
	}
}