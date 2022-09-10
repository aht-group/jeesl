package org.jeesl.util.query.xpath.mail;

import org.jeesl.factory.xml.io.mail.XmlTemplateFactory;
import org.jeesl.model.xml.system.io.mail.Mail;
import org.jeesl.model.xml.system.io.mail.Template;
import org.jeesl.util.query.xpath.MailXpath;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class TestMailsTemplateXpath
{
	final static Logger logger = LoggerFactory.getLogger(TestMailsTemplateXpath.class);
    
	private Template xml1,xml2,xml3,xml4;
	private Mail mail;
	
	@Before
	public void initXml()
	{
		mail = new Mail();
		
		xml1 = XmlTemplateFactory.build("l1", "t1");mail.getTemplate().add(xml1);
		xml2 = XmlTemplateFactory.build("l2", "t2");mail.getTemplate().add(xml2);
		xml3 = XmlTemplateFactory.build("l3", "t3");mail.getTemplate().add(xml3);
		xml4 = XmlTemplateFactory.build("l3", "t3");mail.getTemplate().add(xml4);
	}
	
	@Test
	public void testId1() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		Template actual = MailXpath.getTemplate(mail, xml1.getLang(),xml1.getType());
		Assert.assertEquals(xml1, actual);
	}
	    
	@Test
	public void testId2() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		Template actual = MailXpath.getTemplate(mail, xml2.getLang(),xml2.getType());
		Assert.assertEquals(xml2, actual);
	}

	@Test(expected=ExlpXpathNotFoundException.class)
	public void testNotFound() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		MailXpath.getTemplate(mail, "nullLang", "nullType");
	}
	    
	@Test(expected=ExlpXpathNotUniqueException.class)
	public void testUnique() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		MailXpath.getTemplate(mail, xml3.getLang(),xml3.getType());
	}
}