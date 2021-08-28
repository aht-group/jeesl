package org.jeesl.mail.msg;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.jeesl.model.xml.system.io.mail.Attachment;
import org.jeesl.model.xml.system.io.mail.Image;
import org.jeesl.model.xml.system.io.mail.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.util.xml.JaxbUtil;

public class XmlMimeContentCreator extends AbstractMimeContentCreator
{
	final static Logger logger = LoggerFactory.getLogger(XmlMimeContentCreator.class);
	
	private MimeMessage message;
	
	public XmlMimeContentCreator(MimeMessage message)
	{
		this.message=message;
	}
	
	@Deprecated public void createContent(Mail mail) throws MessagingException
	{		
		JaxbUtil.trace(mail);
		Multipart mpAlternative = new MimeMultipart("alternative");
		mpAlternative.addBodyPart(createTxt(mail));	   
	    
	    if(!mail.isSetAttachment() && !mail.isSetImage())
	    {
	    	message.setContent(mpAlternative);
	    }
	    else
	    {
	    	Multipart mixed = new MimeMultipart("mixed");
	    	
	        MimeBodyPart wrap = new MimeBodyPart();
	        wrap.setContent(mpAlternative);    // HERE'S THE KEY
	        mixed.addBodyPart(wrap);
	       
	        for(Attachment attachment : mail.getAttachment())
	        {
	        	mixed.addBodyPart(createBinary(attachment));
	        }
	        for(Image image : mail.getImage())
	        {
	        	logger.warn("Untested here");
	        	mixed.addBodyPart(createImage(image));
	        }
	        message.setContent(mixed);
	    }
	}
	
	public void buildContent(Mail mail) throws MessagingException
	{		
		JaxbUtil.trace(mail);
		Multipart mpAlternative = new MimeMultipart("alternative");
		mpAlternative.addBodyPart(buildTxt(mail));	   
	    
	    if(!mail.isSetAttachment() && !mail.isSetImage())
	    {
	    	message.setContent(mpAlternative);
	    }
	    else
	    {
	    	Multipart mixed = new MimeMultipart("mixed");
	    	
	        MimeBodyPart wrap = new MimeBodyPart();
	        wrap.setContent(mpAlternative);    // HERE'S THE KEY
	        mixed.addBodyPart(wrap);
	       
	        for(Attachment attachment : mail.getAttachment())
	        {
	        	mixed.addBodyPart(createBinary(attachment));
	        }
	        for(Image image : mail.getImage())
	        {
	        	logger.warn("Untested here");
	        	mixed.addBodyPart(createImage(image));
	        }
	        message.setContent(mixed);
	    }
	}
	
	@Deprecated private MimeBodyPart createTxt(Mail mail) throws MessagingException
	{
		MimeBodyPart txt = new MimeBodyPart();
	
		if(mail.isSetAttachment())
		{
			txt.setContent(mail.getExample()+System.lineSeparator(), "text/plain; charset=\"ISO-8859-1\"");
		}
		else
		{
			txt.setContent(mail.getExample(), "text/plain; charset=\"ISO-8859-1\"");
		}
		
		return txt;
	}
	
	private MimeBodyPart buildTxt(Mail mail) throws MessagingException
	{
		MimeBodyPart txt = new MimeBodyPart();
	
		if(mail.isSetAttachment())
		{
			txt.setContent(mail.getText().getValue()+System.lineSeparator(), "text/plain; charset=\"ISO-8859-1\"");
		}
		else
		{
			txt.setContent(mail.getText().getValue(), "text/plain; charset=\"ISO-8859-1\"");
		}
		
		return txt;
	}
}
