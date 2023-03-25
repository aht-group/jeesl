package org.jeesl.model.xml.system.io.report;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.system.status.TestXmlCategory;
import org.jeesl.model.xml.system.status.TestXmlDescriptions;
import org.jeesl.model.xml.system.status.TestXmlLangs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.report.Report;

public class TestXmlReport extends AbstractXmlReportTest<Report>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlReport.class);
	
	public TestXmlReport(){super(Report.class);}
	public static Report create(boolean withChildren){return (new TestXmlReport()).build(withChildren);}
    
    public Report build(boolean withChildren)
    {
    	Report xml = new Report();
    	xml.setId("myId");
    	xml.setCode("myCode");
    	xml.setPosition(1);
    	xml.setVisible(true);
    	
    	xml.setDir("testDir");
    	xml.setExample("testExampleXmlFile");
    	xml.setXmlExample("xmlExampleFile");
    	xml.getMedia().add(TestXmlMedia.create(false));
    	xml.getMedia().add(TestXmlMedia.create(false));
    	xml.setLtr(true);
    	xml.setRtl(false);
    	xml.setTest("myTestClass");
    	
    	if(withChildren)
    	{
    		xml.setLangs(TestXmlLangs.create(false));
    		xml.setDescriptions(TestXmlDescriptions.create(false));
    		xml.setCategory(TestXmlCategory.create(false));
    		xml.setXlsWorkbook(TestXmlXlsWorkbook.create(false));
    		
//    		xml.getTimeSeries().add(TestXmlTimeSeries.create(false));xml.getTimeSeries().add(TestXmlTimeSeries.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlReport test = new TestXmlReport();
		test.saveReferenceXml();
    }
}