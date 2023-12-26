package org.jeesl.model.xml.system.io.report;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.report.DataAssociation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlDataAssociation extends AbstractXmlReportTest<DataAssociation>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlDataAssociation.class);
	
	public TestXmlDataAssociation(){super(DataAssociation.class);}
	public static DataAssociation create(boolean withChildren){return (new TestXmlDataAssociation()).build(withChildren);}   
    
    public DataAssociation build(boolean withChildren)
    {
    	DataAssociation xml = new DataAssociation();
    	xml.setColumn("A");
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlDataAssociation test = new TestXmlDataAssociation();
		test.saveReferenceXml();
    }
}