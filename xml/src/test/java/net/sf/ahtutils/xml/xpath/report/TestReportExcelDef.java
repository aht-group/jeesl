package net.sf.ahtutils.xml.xpath.report;

import java.util.ArrayList;
import java.util.List;

import net.sf.ahtutils.test.AbstractXmlTest;
import net.sf.ahtutils.xml.report.Media;
import net.sf.ahtutils.xml.report.TestXmlMedia;
import net.sf.ahtutils.xml.report.XlsDefinition;
import net.sf.ahtutils.xml.report.XlsWorkbook;
import net.sf.ahtutils.xml.xpath.ReportXpath;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;
import net.sf.exlp.util.xml.JaxbUtil;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestReportExcelDef extends AbstractXmlTest
{
	final static Logger logger = LoggerFactory.getLogger(TestReportExcelDef.class);
    
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