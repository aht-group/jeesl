package org.jeesl.model.xml.system.io.report;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.report.XlsSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlXlsSheet extends AbstractXmlReportTest<XlsSheet>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlXlsSheet.class);
	
	public TestXmlXlsSheet(){super(XlsSheet.class);}
	public static XlsSheet create(boolean withChildren){return (new TestXmlXlsSheet()).build(withChildren);}
    
    public XlsSheet build(boolean withChildren)
    {
    	XlsSheet xml = new XlsSheet();
    	xml.setCode("myCode");
    	xml.setPosition(1);
    	xml.setVisible(true);
    	xml.setQuery("myQuery");
    	xml.setPrimaryKey("myPK");
    	
    	if(withChildren)
    	{
    		
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlXlsSheet test = new TestXmlXlsSheet();
		test.saveReferenceXml();
    }
}