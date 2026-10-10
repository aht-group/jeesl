package org.jeesl.util.query.xpath.status;

import org.jeesl.AbstractJeeslUtilTest;
import org.jeesl.model.xml.io.locale.status.Description;
import org.jeesl.model.xml.io.locale.status.Descriptions;
import org.jeesl.model.xml.system.status.TestXmlDescription;
import org.jeesl.model.xml.system.status.TestXmlDescriptions;
import org.jeesl.util.query.xpath.StatusXpath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class TestStatusXPathDescription extends AbstractJeeslUtilTest
{
	final static Logger logger = LoggerFactory.getLogger(TestStatusXPathDescription.class);
    
	private Descriptions descriptions;
	private Description d1,d2,d3;
	
	@BeforeEach
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
	    Assertions.assertEquals(d1,actual);
	}

	@Test
	public void testNotFound() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		Assertions.assertThrows(ExlpXpathNotFoundException.class,() -> {
		StatusXpath.getDescription(descriptions, "-1");
	});
	}
	
	 @Test
	 public void testUnique() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	 {
		Assertions.assertThrows(ExlpXpathNotUniqueException.class,() -> {
		 StatusXpath.getDescription(descriptions, d2.getKey());
	 });
	}
}