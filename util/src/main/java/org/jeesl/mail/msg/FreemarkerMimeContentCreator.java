package org.jeesl.mail.msg;

import java.io.IOException;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.xml.parsers.ParserConfigurationException;

import org.jdom2.Document;
import org.jeesl.controller.mail.freemarker.FreemarkerEngine;
import org.jeesl.exception.processing.UtilsMailException;
import org.jeesl.model.xml.system.io.mail.Attachment;
import org.jeesl.model.xml.system.io.mail.Image;
import org.jeesl.model.xml.system.io.mail.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import freemarker.template.TemplateException;

public class FreemarkerMimeContentCreator extends AbstractMimeContentCreator
{
	final static Logger logger = LoggerFactory.getLogger(FreemarkerMimeContentCreator.class);
	
	private MimeMessage message;
	private FreemarkerEngine fme;
	private static final String encoding = "ISO-8859-1";
	
	public FreemarkerMimeContentCreator(MimeMessage message, FreemarkerEngine fme)
	{
		this.message=message;
		this.fme=fme;
	}
	
	public void createContent(Document xml, Mail mail) throws MessagingException, UtilsMailException
	{		
		boolean txtBody = fme.isAvailable(mail.getCode(), mail.getLang(), "txt");
		boolean htmlBody = fme.isAvailable(mail.getCode(), mail.getLang(), "html");
		if(!txtBody && !htmlBody){throw new UtilsMailException("No template available for "+mail.getCode()+"/"+mail.getLang());}
		
		MimeBodyPart mbpTxt=null;
		MimeBodyPart mbpHtml=null;
		
		if(txtBody){mbpTxt = createTxt(mail.getLang(),xml,mail);}
		if(htmlBody)
		{
			mbpHtml = createHtml(mail.getLang(),xml,mail);
//			mbpHtml.setDisposition(BodyPart.INLINE);
		}
		
		Multipart mpAlternative = new MimeMultipart("alternative");
    	if(txtBody){mpAlternative.addBodyPart(mbpTxt);}
    	if(htmlBody){mpAlternative.addBodyPart(mbpHtml);}
		
	    if(!mail.isSetAttachment() && !mail.isSetImage())
	    {
	    	message.setContent(mpAlternative);
	    }
	    else
	    {
	    	Multipart mixed = new MimeMultipart("mixed");
	        MimeBodyPart wrap = new MimeBodyPart();
	        wrap.setContent(mpAlternative);
	        mixed.addBodyPart(wrap);
	       
	        for(Attachment attachment : mail.getAttachment())
	        {
	        	mixed.addBodyPart(createBinary(attachment));
	        }
	        for(Image image : mail.getImage())
	        {
	        	logger.error("Adding image");
	        	mixed.addBodyPart(createImage(image));
	        }
	        message.setContent(mixed);
	    }
	}
	
	private MimeBodyPart createTxt(String lang, Document xml, Mail mail) throws MessagingException
	{
		fme.createTemplate(mail.getCode(), lang, "txt");
		
		MimeBodyPart txt = new MimeBodyPart();
	
		try
		{
			if(mail.isSetAttachment())
			{
				txt.setContent(fme.process(xml)+System.lineSeparator(), "text/plain; charset=\""+encoding+"\"");
			}
			else{txt.setContent(fme.process(xml), "text/plain; charset=\""+encoding+"\"");}
		}
		catch (SAXException e) {e.printStackTrace();}
		catch (IOException e) {e.printStackTrace();}
		catch (ParserConfigurationException e) {e.printStackTrace();}
		catch (TemplateException e) {e.printStackTrace();}
		
		return txt;
	}
	
	private MimeBodyPart createHtml(String lang,Document xml, Mail mail) throws MessagingException
	{
		fme.createTemplate(mail.getCode(), lang, "html");
		MimeBodyPart html = new MimeBodyPart();
		try
		{
			if(mail.isSetAttachment())
			{
				html.setContent(fme.process(xml)+System.lineSeparator(), "text/html; charset=\""+encoding+"\"");

			}
			else{html.setContent(fme.process(xml), "text/html; charset=\""+encoding+"\"");}
			
		}
		catch (SAXException e) {e.printStackTrace();}
		catch (IOException e) {e.printStackTrace();}
		catch (ParserConfigurationException e) {e.printStackTrace();}
		catch (TemplateException e) {e.printStackTrace();}
		
		return html;
	}
}