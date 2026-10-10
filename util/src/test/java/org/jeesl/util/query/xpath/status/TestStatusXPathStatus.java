package org.jeesl.util.query.xpath.status;

import org.jeesl.AbstractJeeslUtilTest;
import org.jeesl.model.xml.io.locale.status.Status;
import org.jeesl.model.xml.jeesl.TestXmlAht;
import org.jeesl.model.xml.system.status.TestXmlStatus;
import org.jeesl.model.xml.xsd.aht.Aht;
import org.jeesl.util.query.xpath.StatusXpath;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class TestStatusXPathStatus extends AbstractJeeslUtilTest
{
	final static Logger logger = LoggerFactory.getLogger(TestStatusXPathStatus.class);
    
	private Aht aht;
	private Status s1,s2,s3;
	
	@BeforeEach
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
	    Assertions.assertEquals(s1,actual);
	}

	@Test
	public void testNotFound() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		Assertions.assertThrows(ExlpXpathNotFoundException.class,() -> {
		StatusXpath.getStatus(aht.getStatus(), "-1");
	});
	}
	
	 @Test
	 public void testUnique() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	 {
		Assertions.assertThrows(ExlpXpathNotUniqueException.class,() -> {
		 StatusXpath.getStatus(aht.getStatus(), s2.getCode());
	 });
	}
}