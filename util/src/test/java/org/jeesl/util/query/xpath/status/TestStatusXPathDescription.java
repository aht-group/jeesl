package org.jeesl.util.query.xpath.status;

import org.jeesl.AbstractJeeslUtilTest;
import org.jeesl.model.xml.io.locale.status.Description;
import org.jeesl.model.xml.io.locale.status.Descriptions;
import org.jeesl.model.xml.system.status.TestXmlDescription;
import org.jeesl.model.xml.system.status.TestXmlDescriptions;
import org.jeesl.util.query.xpath.StatusXpath;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class TestStatusXPathDescription extends AbstractJeeslUtilTest
{
	final static Logger logger = LoggerFactory.getLogger(TestStatusXPathDescription.class);
    
	private Descriptions descriptions;
	private Description d1,d2,d3;
	
	@Before
	public void iniDbseed()
	{
		descriptions = TestXmlDescriptions.create(false);

		d1 = TestXmlDescription.create(false);d1.setKey("ok");descriptions.getDescription().add(d1);
		d2 = TestXmlDescription.create(false);d2.setKey("multi");descriptions.getDescription().add(d2);
		d3 = TestXmlDescription.create(false);d3.setKey("multi");descriptions.getDescription().add(d3);
	}
	
	@Test
	public void find() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		Description actual = StatusXpath.getDescription(descriptions, d1.getKey());
	    Assert.assertEquals(d1,actual);
	}

	@Test(expected=ExlpXpathNotFoundException.class)
	public void testNotFound() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		StatusXpath.getDescription(descriptions, "-1");
	}
	
	 @Test(expected=ExlpXpathNotUniqueException.class)
	 public void testUnique() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	 {
		 StatusXpath.getDescription(descriptions, d2.getKey());
	 }
}