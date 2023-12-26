package org.jeesl.model.xml.system.io.report;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.report.XlsTransformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlXlsTransformation extends AbstractXmlReportTest<XlsTransformation>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlXlsTransformation.class);
	
	public TestXmlXlsTransformation(){super(XlsTransformation.class);}
	public static XlsTransformation create(boolean withChildren){return (new TestXmlXlsTransformation()).build(withChildren);}
    
    public XlsTransformation build(boolean withChildren)
    {
    	XlsTransformation xml = new XlsTransformation();
    	
    	xml.setDataClass("myDataClass");
    	xml.setFormatPattern("myPattern");
    	xml.setBeanProperty("myBeanProperty");
    	
    	if(withChildren)
    	{

    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlXlsTransformation test = new TestXmlXlsTransformation();
		test.saveReferenceXml();
    }
}