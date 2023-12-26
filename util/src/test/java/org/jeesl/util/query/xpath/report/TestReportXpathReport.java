package org.jeesl.util.query.xpath.report;

import org.jeesl.AbstractJeeslUtilTest;
import org.jeesl.model.xml.io.report.Report;
import org.jeesl.model.xml.io.report.Reports;
import org.jeesl.model.xml.system.io.report.TestXmlReport;
import org.jeesl.util.query.xpath.ReportXpath;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;

public class TestReportXpathReport extends AbstractJeeslUtilTest
{
	final static Logger logger = LoggerFactory.getLogger(TestReportXpathReport.class);
    
	private Report xml1,xml2,xml3,xml4;
	private Reports reports;
	
	@Before
	public void iniMedia()
	{
		reports = new Reports();
		
		xml1 = TestXmlReport.create(false);xml1.setId("t1");reports.getReport().add(xml1);
		xml2 = TestXmlReport.create(false);xml2.setId("t2");reports.getReport().add(xml2);
		xml3 = TestXmlReport.create(false);xml3.setId("t3");reports.getReport().add(xml3);
		xml4 = TestXmlReport.create(false);xml4.setId("t3");reports.getReport().add(xml4);
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