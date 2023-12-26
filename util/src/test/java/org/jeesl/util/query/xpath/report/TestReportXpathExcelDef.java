package org.jeesl.util.query.xpath.report;

import org.jeesl.AbstractJeeslUtilTest;
import org.jeesl.model.xml.io.report.XlsDefinition;
import org.jeesl.model.xml.io.report.XlsWorkbook;
import org.jeesl.util.query.xpath.ReportXpath;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class TestReportXpathExcelDef extends AbstractJeeslUtilTest
{
	final static Logger logger = LoggerFactory.getLogger(TestReportXpathExcelDef.class);
    
	private XlsDefinition def;
	
	@Before
	public void iniDef()
	{
		def = new XlsDefinition();
    	
		XlsWorkbook one = new XlsWorkbook();
		one.setCode("one");
		
		XlsWorkbook two = new XlsWorkbook();
		two.setCode("two");
		
		def.getXlsWorkbook().add(one);
		def.getXlsWorkbook().add(two);
	}
	
	   @Test
	    public void testXPath() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	    {
	    	XlsWorkbook test = ReportXpath.getWorkbook(def, "two");
	    	Assert.assertEquals(test.getCode(),"two");
	    }
}