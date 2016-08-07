package org.jeesl.util.query.xpath.report;

import org.jeesl.AbstractJeeslUtilTest;
import org.jeesl.model.xml.report.TestXmlReport;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.report.Jr;
import net.sf.ahtutils.xml.report.Report;
import net.sf.ahtutils.xml.report.Reports;
import net.sf.ahtutils.xml.xpath.ReportXpath;
import net.sf.exlp.exception.ExlpXpathNotFoundException;
import net.sf.exlp.exception.ExlpXpathNotUniqueException;
import net.sf.exlp.util.xml.JaxbUtil;

public class TestReportsMrXpath extends AbstractJeeslUtilTest
{
	final static Logger logger = LoggerFactory.getLogger(TestReportsMrXpath.class);
    
	private Report xml1,xml2,xml3,xml4;
	private Reports reports;
	
	@Before
	public void iniMedia()
	{
		reports = new Reports();
		
		xml1 = TestXmlReport.create(false);xml1.setId("t1");
		xml1.getMedia().get(0).getJr().get(0).setName("n1");xml1.getMedia().get(0).setType("pdf");
		reports.getReport().add(xml1);
		
		xml2 = TestXmlReport.create(false);xml2.setId("t2");
		xml2.getMedia().get(0).getJr().get(0).setName("n2");xml2.getMedia().get(0).setType("pdf");
		reports.getReport().add(xml2);
		xml3 = TestXmlReport.create(false);xml3.setId("t3");reports.getReport().add(xml3);xml3.getMedia().get(0).setType("pdf");xml3.getMedia().get(1).setType("pdf");
		xml4 = TestXmlReport.create(false);xml3.setId("t3");reports.getReport().add(xml4);
	}
	
	@Test
	public void testId1() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		JaxbUtil.info(reports);
		Jr actual = ReportXpath.getMr(reports, xml1.getId(), "pdf");
		assertJaxbEquals(xml1.getMedia().get(0).getJr().get(0), actual);
	}
	    
	@Test
	public void testId2() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		Jr actual = ReportXpath.getMr(reports, xml2.getId(), "pdf");
		assertJaxbEquals(xml2.getMedia().get(0).getJr().get(0), actual);
	}

	@Test(expected=ExlpXpathNotFoundException.class)
	public void testNotFoundId() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		ReportXpath.getMr(reports, "nullCode", "pdf");
	}
	
	@Test(expected=ExlpXpathNotFoundException.class)
	public void testNotFoundMedia() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		ReportXpath.getMr(reports, xml1.getId(), "nullMedia");
	}
	    
	@Test(expected=ExlpXpathNotUniqueException.class)
	public void testUnique() throws ExlpXpathNotFoundException, ExlpXpathNotUniqueException
	{
		ReportXpath.getMr(reports, xml3.getId(), "pdf");
	}
	
}