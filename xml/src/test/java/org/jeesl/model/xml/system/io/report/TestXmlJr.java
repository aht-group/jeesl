package org.jeesl.model.xml.system.io.report;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.report.Jr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlJr extends AbstractXmlReportTest<Jr>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlJr.class);
	
	public TestXmlJr(){super(Jr.class);}
	public static Jr create(boolean withChildren){return (new TestXmlJr()).build(withChildren);}   
    
    public Jr build(boolean withChildren)
    {
    	Jr jr = new Jr();
    	jr.setName("testReportName");
    	jr.setType("mr");
    	jr.setAlternateDir("altDir");
    	jr.setVisible(true);
    	return jr;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlJr test = new TestXmlJr();
		test.saveReferenceXml();
    }
}