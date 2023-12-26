package org.jeesl.model.xml.system.io.report;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.report.Offset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlOffset extends AbstractXmlReportTest<Offset>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlOffset.class);
	
	public TestXmlOffset(){super(Offset.class);}
	public static Offset create(boolean withChildren){return (new TestXmlOffset()).build(withChildren);}   
    
    public Offset build(boolean withChildren)
    {
    	Offset xml = new Offset();
    	xml.setRows(2);
    	xml.setColumns(3);
    	
    	if(withChildren)
    	{
    		
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlOffset test = new TestXmlOffset();
		test.saveReferenceXml();
    }
}