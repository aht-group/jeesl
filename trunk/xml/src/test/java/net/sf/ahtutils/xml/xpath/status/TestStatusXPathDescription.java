package net.sf.ahtutils.xml.xpath.status;

import net.sf.ahtutils.test.AbstractXmlTest;
import net.sf.ahtutils.xml.status.Description;
import net.sf.ahtutils.xml.status.Descriptions;
import net.sf.ahtutils.xml.status.TestXmlDescription;
import net.sf.ahtutils.xml.status.TestXmlDescriptions;
import net.sf.ahtutils.xml.xpath.StatusXpath;
import net.sf.exlp.util.exception.ExlpXpathNotFoundException;
import net.sf.exlp.util.exception.ExlpXpathNotUniqueException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestStatusXPathDescription extends AbstractXmlTest
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