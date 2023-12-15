package org.jeesl.util.query.xpath.status;

import org.jeesl.AbstractJeeslUtilTest;
import org.jeesl.model.xml.io.locale.status.Status;
import org.jeesl.model.xml.jeesl.TestXmlAht;
import org.jeesl.model.xml.system.status.TestXmlStatus;
import org.jeesl.util.query.xpath.StatusXpath;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.aht.Aht;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class TestStatusXPathStatus extends AbstractJeeslUtilTest
{
	final static Logger logger = LoggerFactory.getLogger(TestStatusXPathStatus.class);
    
	private Aht aht;
	private Status s1,s2,s3;
	
	@Before
	public void iniDbseed()
	{
		aht = TestXmlAht.create(false);

		s1 = TestXmlStatus.create(false);s1.setCode("ok");aht.getStatus().add(s1);
		s2 = TestXmlStatus.create(false);s2.setCode("multi");aht.getStatus().add(s2);
		s3 = TestXmlStatus.create(false);s3.setCode("multi");aht.getStatus().add(s3);
	}
	
	@Test
	public void find() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		Status actual = StatusXpath.getStatus(aht.getStatus(), s1.getCode());
	    Assert.assertEquals(s1,actual);
	}

	@Test(expected=ExlpXpathNotFoundException.class)
	public void testNotFound() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		StatusXpath.getStatus(aht.getStatus(), "-1");
	}
	
	 @Test(expected=ExlpXpathNotUniqueException.class)
	 public void testUnique() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	 {
		 StatusXpath.getStatus(aht.getStatus(), s2.getCode());
	 }
}