package net.sf.ahtutils.xml.xpath.report;

import net.sf.ahtutils.test.AbstractXmlTest;
import net.sf.ahtutils.xml.report.Report;
import net.sf.ahtutils.xml.report.Reports;
import net.sf.ahtutils.xml.report.TestReport;
import net.sf.ahtutils.xml.xpath.ReportXpath;
import net.sf.exlp.util.exception.ExlpXpathNotFoundException;
import net.sf.exlp.util.exception.ExlpXpathNotUniqueException;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestReportsReportXpath extends AbstractXmlTest
{
	final static Logger logger = LoggerFactory.getLogger(TestReportsReportXpath.class);
    
	private Report xml1,xml2,xml3,xml4;
	private Reports reports;
	
	@Before
	public void iniMedia()
	{
		reports = new Reports();
		
		xml1 = TestReport.create("t1");reports.getReport().add(xml1);
		xml2 = TestReport.create("t2");reports.getReport().add(xml2);
		xml3 = TestReport.create("t3");reports.getReport().add(xml3);
		xml4 = TestReport.create("t3");reports.getReport().add(xml4);
	}
	
	@Test
	public void testId1() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		Report actual = ReportXpath.getReport(reports, xml1.getId());
		assertJaxbEquals(xml1, actual);
	}
	    
	@Test
	public void testId2() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		Report actual = ReportXpath.getReport(reports, xml2.getId());
		assertJaxbEquals(xml2, actual);
	}

	@Test(expected=ExlpXpathNotFoundException.class)
	public void testNotFound() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		 ReportXpath.getReport(reports, "nullCode");
	}
	    
	@Test(expected=ExlpXpathNotUniqueException.class)
	public void testUnique() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		ReportXpath.getReport(reports, xml3.getId());
	}
}