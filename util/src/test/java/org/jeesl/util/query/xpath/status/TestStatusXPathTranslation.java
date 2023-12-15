package org.jeesl.util.query.xpath.status;

import org.jeesl.AbstractJeeslUtilTest;
import org.jeesl.model.xml.io.locale.status.Translation;
import org.jeesl.model.xml.io.locale.status.Translations;
import org.jeesl.model.xml.system.status.TestXmlTranslation;
import org.jeesl.model.xml.system.status.TestXmlTranslations;
import org.jeesl.util.query.xpath.StatusXpath;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class TestStatusXPathTranslation extends AbstractJeeslUtilTest
{
	final static Logger logger = LoggerFactory.getLogger(TestStatusXPathTranslation.class);
    
	private Translations translations;
	private Translation l1,l2,l3;
	
	@Before
	public void iniDbseed()
	{
		translations = TestXmlTranslations.create(false);

		l1 = TestXmlTranslation.create(false);l1.setKey("ok");translations.getTranslation().add(l1);
		l2 = TestXmlTranslation.create(false);l2.setKey("multi");translations.getTranslation().add(l2);
		l3 = TestXmlTranslation.create(false);l3.setKey("multi");translations.getTranslation().add(l3);
	}
	
	@Test
	public void find() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		Translation actual = StatusXpath.getTranslation(translations, l1.getKey());
	    Assert.assertEquals(l1,actual);
	}

	@Test(expected=ExlpXpathNotFoundException.class)
	public void testNotFound() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		StatusXpath.getTranslation(translations, "-1");
	}
	
	 @Test(expected=ExlpXpathNotUniqueException.class)
	 public void testUnique() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	 {
		 StatusXpath.getTranslation(translations, l2.getKey());
	 }
}