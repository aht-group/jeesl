package org.jeesl.controller.io.mail;

import org.jeesl.exception.processing.UtilsConfigurationException;
import org.jeesl.model.xml.io.mail.EmailAddress;
import org.jeesl.model.xml.io.mail.From;
import org.jeesl.model.xml.io.mail.Header;
import org.jeesl.model.xml.io.mail.To;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractJeeslMailCreator
{
	final static Logger logger = LoggerFactory.getLogger(AbstractJeeslMailCreator.class);
		
	protected String lang;
	protected Header header;
	protected String mailCode;
	
	protected boolean doneInjection,doneWork;
	
	protected EmailAddress fromAddress;
	protected String subject;
	
	public AbstractJeeslMailCreator(String mailCode)
	{
		lang = "de";
		this.mailCode=mailCode;
		doneInjection=false;
		doneWork=false;
	}
	
//	public abstract void x();
	
	protected void setInjections(){doneInjection=true;}
	protected void setWork(){doneWork=true;}
	
	protected void checkPreconditions() throws UtilsConfigurationException
	{
		if(doneInjection==false){throw new UtilsConfigurationException("No Injections done");}
		if(doneWork==false){throw new UtilsConfigurationException("No Work defined");}
	}
	
	protected void createHeader()
	{
		header = new Header();
		header.setSubject(subject);
				
		From from = new From();
		from.setEmailAddress(fromAddress);
		header.setFrom(from);		
		
		header.setTo(new To());
	}
}