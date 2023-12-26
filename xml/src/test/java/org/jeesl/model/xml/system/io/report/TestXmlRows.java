package org.jeesl.model.xml.system.io.report;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.report.Rows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlRows extends AbstractXmlReportTest<Rows>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlRows.class);
	
	public TestXmlRows(){super(Rows.class);}
	public static Rows create(boolean withChildren){return (new TestXmlRows()).build(withChildren);}   
    
    public Rows build(boolean withChildren)
    {
    	Rows xml = new Rows();
    	
    	if(withChildren)
    	{
    		xml.getRow().add(TestXmlRow.create(false));xml.getRow().add(TestXmlRow.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlRows test = new TestXmlRows();
		test.saveReferenceXml();
    }
}