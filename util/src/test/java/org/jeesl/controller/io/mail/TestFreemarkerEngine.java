package org.jeesl.controller.io.mail;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.jeesl.AbstractJeeslUtilTest;
import org.jeesl.controller.io.mail.freemarker.FreemarkerEngine;
import org.jeesl.exception.processing.JeeslDeveloperException;
import org.jeesl.model.xml.io.mail.Mail;
import org.jeesl.model.xml.io.mail.Mails;
import org.jeesl.model.xml.io.mail.Template;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import freemarker.template.TemplateException;

public class TestFreemarkerEngine extends AbstractJeeslUtilTest
{
	final static Logger logger = LoggerFactory.getLogger(TestFreemarkerEngine.class);
	
	private FreemarkerEngine fme;
	
	private Mails mails;
	
	@BeforeEach
	public void init()
	{	
		mails = new Mails();
		
		Mail mail = new Mail();
		mail.setCode("id");
		
		Template template = new Template();
		template.setLang("de");
		template.setType("html");
		
		mail.getTemplate().add(template);
		mails.getMail().add(mail);
		
		fme = new FreemarkerEngine(mails);
	}
	
	@AfterEach
	public void close()
	{
		fme = null;
	}
    
	@Disabled
    @Test
    public void devException() throws SAXException, IOException, ParserConfigurationException, TemplateException
    {
		Assertions.assertThrows(JeeslDeveloperException.class,() -> {
    	fme.processXml("test");
    });
	}
    
    @Test
    public void isAvailable() throws SAXException, IOException, ParserConfigurationException, TemplateException
    {
    	Assertions.assertFalse(fme.isAvailable("null", "de", "txt"));
    	Assertions.assertFalse(fme.isAvailable("id", "de", "txt"));
    	Assertions.assertTrue(fme.isAvailable("id", "de", "html"));
    }
}