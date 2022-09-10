package org.jeesl.util.query.xpath.mail;

import org.jeesl.factory.xml.io.mail.XmlMailFactory;
import org.jeesl.model.xml.system.io.mail.Mail;
import org.jeesl.model.xml.system.io.mail.Mails;
import org.jeesl.util.query.xpath.MailXpath;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;
import net.sf.exlp.util.xml.JaxbUtil;

public class TestMailsMailXpath
{
	final static Logger logger = LoggerFactory.getLogger(TestMailsMailXpath.class);
    
	private Mail xml1,xml2,xml3,xml4;
	private Mails mails;
	
	@Before
	public void iniMedia()
	{
		mails = new Mails();
		
		xml1 = XmlMailFactory.build("t1");;mails.getMail().add(xml1);
		xml2 = XmlMailFactory.build("t2");;mails.getMail().add(xml2);
		xml3 = XmlMailFactory.build("t3");mails.getMail().add(xml3);
		xml4 = XmlMailFactory.build("t3");mails.getMail().add(xml4);
		JaxbUtil.trace(mails);
	}
	
	@Test
	public void testId1() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		Mail actual = MailXpath.getMail(mails, xml1.getCode());
		Assert.assertEquals(xml1, actual);
	}
	    
	@Test
	public void testId2() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		Mail actual = MailXpath.getMail(mails, xml2.getCode());
		Assert.assertEquals(xml2, actual);
	}

	@Test(expected=ExlpXpathNotFoundException.class)
	public void testNotFound() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		MailXpath.getMail(mails, "nullCode");
	}
	    
	@Test(expected=ExlpXpathNotUniqueException.class)
	public void testUnique() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		MailXpath.getMail(mails, xml3.getCode());
	}
}