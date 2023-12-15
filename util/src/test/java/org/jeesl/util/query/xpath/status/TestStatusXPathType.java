package org.jeesl.util.query.xpath.status;

import org.jeesl.AbstractJeeslUtilTest;
import org.jeesl.model.xml.io.locale.status.Type;
import org.jeesl.model.xml.io.locale.status.Types;
import org.jeesl.model.xml.system.status.TestXmlType;
import org.jeesl.model.xml.system.status.TestXmlTypes;
import org.jeesl.util.query.xpath.StatusXpath;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class TestStatusXPathType extends AbstractJeeslUtilTest
{
	final static Logger logger = LoggerFactory.getLogger(TestStatusXPathType.class);
    
	private Types types;
	private Type t1,t2,t3;
	
	@Before
	public void iniDbseed()
	{
		types = TestXmlTypes.create(false);

		t1 = TestXmlType.create(false);t1.setKey("ok");types.getType().add(t1);
		t2 = TestXmlType.create(false);t2.setKey("multi");types.getType().add(t2);
		t3 = TestXmlType.create(false);t3.setKey("multi");types.getType().add(t3);
	}
	
	@Test
	public void find() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		Type actual = StatusXpath.getType(types, t1.getKey());
	    Assert.assertEquals(t1,actual);
	}

	@Test(expected=ExlpXpathNotFoundException.class)
	public void testNotFound() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		StatusXpath.getType(types, "-1");
	}
	
	 @Test(expected=ExlpXpathNotUniqueException.class)
	 public void testUnique() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	 {
		 StatusXpath.getType(types, t2.getKey());
	 }
}