package org.jeesl.model.xml.system.io.report;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.report.XlsDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlXlsDefinition extends AbstractXmlReportTest<XlsDefinition>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlXlsDefinition.class);
	
	public TestXmlXlsDefinition(){super(XlsDefinition.class);}
	public static XlsDefinition create(boolean withChildren){return (new TestXmlXlsDefinition()).build(withChildren);}
    
    public XlsDefinition build(boolean withChildren)
    {
    	XlsDefinition xml = new XlsDefinition();
    	
    	if(withChildren)
    	{
    		xml.getXlsWorkbook().add(TestXmlXlsWorkbook.create(false));
    		xml.getXlsWorkbook().add(TestXmlXlsWorkbook.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlXlsDefinition test = new TestXmlXlsDefinition();
		test.saveReferenceXml();
    }
}