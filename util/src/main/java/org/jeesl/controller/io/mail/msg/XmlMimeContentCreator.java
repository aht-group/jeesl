package org.jeesl.controller.io.mail.msg;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang3.ObjectUtils;
import org.jeesl.model.xml.io.mail.Attachment;
import org.jeesl.model.xml.io.mail.Image;
import org.jeesl.model.xml.io.mail.Mail;
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

	public void buildContent(Mail mail) throws MessagingException
	{		
		JaxbUtil.trace(mail);
		Multipart mpAlternative = new MimeMultipart("alternative");
		mpAlternative.addBodyPart(buildTxt(mail));	   
	    
	    if(ObjectUtils.isEmpty(mail.getAttachment()) && ObjectUtils.isEmpty(mail.getImage()))
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
	
	private MimeBodyPart buildTxt(Mail mail) throws MessagingException
	{
		MimeBodyPart txt = new MimeBodyPart();
	
		if(ObjectUtils.isNotEmpty(mail.getAttachment()))
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