package org.jeesl.model.xml.system.io.report;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.report.XlsWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlXlsWorkbook extends AbstractXmlReportTest<XlsWorkbook>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlXlsWorkbook.class);
	
	public TestXmlXlsWorkbook(){super(XlsWorkbook.class);}
	public static XlsWorkbook create(boolean withChildren){return (new TestXmlXlsWorkbook()).build(withChildren);} 
    
    public XlsWorkbook build(boolean withChildren)
    {
    	XlsWorkbook xml = new XlsWorkbook();
    	xml.setCode("myCode");
    	
    	if(withChildren)
    	{
    		xml.setXlsSheets(TestXmlXlsSheets.create(false));
    		xml.getXlsSheet().add(TestXmlXlsSheet.create(false));
    		xml.getXlsSheet().add(TestXmlXlsSheet.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlXlsWorkbook test = new TestXmlXlsWorkbook();
		test.saveReferenceXml();
    }
}