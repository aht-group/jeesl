package org.jeesl.model.xml.system.io.report;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.report.Size;
import org.jeesl.model.xml.system.status.TestXmlType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlSize extends AbstractXmlReportTest<Size>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlSize.class);
	
	public TestXmlSize(){super(Size.class);}
	public static Size create(boolean withChildren){return (new TestXmlSize()).build(withChildren);}   
    
    public Size build(boolean withChildren)
    {
    	Size xml = new Size();
    	xml.setCode("myCode");
    	
    	if(withChildren)
    	{
    		xml.setType(TestXmlType.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlSize test = new TestXmlSize();
		test.saveReferenceXml();
    }
}