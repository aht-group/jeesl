package net.sf.ahtutils.xml.xpath.status;

import net.sf.ahtutils.test.AbstractXmlTest;
import net.sf.ahtutils.xml.status.TestXmlType;
import net.sf.ahtutils.xml.status.TestXmlTypes;
import net.sf.ahtutils.xml.status.Type;
import net.sf.ahtutils.xml.status.Types;
import net.sf.ahtutils.xml.xpath.StatusXpath;
import net.sf.exlp.util.exception.ExlpXpathNotFoundException;
import net.sf.exlp.util.exception.ExlpXpathNotUniqueException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestStatusXPathType extends AbstractXmlTest
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