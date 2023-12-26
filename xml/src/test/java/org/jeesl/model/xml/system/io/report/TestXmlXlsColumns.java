package org.jeesl.model.xml.system.io.report;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.report.XlsColumns;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlXlsColumns extends AbstractXmlReportTest<XlsColumns>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlXlsColumns.class);
	
	public TestXmlXlsColumns(){super(XlsColumns.class);}
	public static XlsColumns create(boolean withChildren){return (new TestXmlXlsColumns()).build(withChildren);}
    
    public XlsColumns build(boolean withChildren)
    {
    	XlsColumns xml = new XlsColumns();
    	
    	xml.setQuery("myQuery");
    	
    	if(withChildren)
    	{
    		xml.getXlsColumn().add(TestXmlXlsColumn.create(false));xml.getXlsColumn().add(TestXmlXlsColumn.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlXlsColumns test = new TestXmlXlsColumns();
		test.saveReferenceXml();
    }
}