package org.jeesl.model.xml.system.io.report;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.report.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlFont extends AbstractXmlReportTest<Font>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlFont.class);
	
	public TestXmlFont(){super(Font.class);}
	public static Font create(boolean withChildren){return (new TestXmlFont()).build(withChildren);}   
    
    public Font build(boolean withChildren)
    {
    	Font xml = new Font();
    	
    	xml.setItalic(true);
    	xml.setBold(true);
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlFont test = new TestXmlFont();
		test.saveReferenceXml();
    }
}