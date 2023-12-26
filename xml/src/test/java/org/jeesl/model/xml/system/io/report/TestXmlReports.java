package org.jeesl.model.xml.system.io.report;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.report.Reports;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlReports extends AbstractXmlReportTest<Reports>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlReports.class);
	
	public TestXmlReports(){super(Reports.class);}
	public static Reports create(boolean withChildren){return (new TestXmlReports()).build(withChildren);}
    
    public Reports build(boolean withChildren)
    {
    	Reports reports = new Reports();
    	reports.setDir("testDir");
    	reports.setResources("reports.ahtutils-util/resources.xml");
    	reports.setTemplates("reports.ahtutils-util/templates.xml");
    	
    	if(withChildren)
    	{
    		reports.getReport().add(TestXmlReport.create(false));
    		reports.getReport().add(TestXmlReport.create(false));
    	}
    	
    	return reports;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlReports test = new TestXmlReports();
		test.saveReferenceXml();
    }
}