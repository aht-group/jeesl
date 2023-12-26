package org.jeesl.model.xml.system.io.report;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.report.Hash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlHash extends AbstractXmlReportTest<Hash>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlHash.class);
	
	public TestXmlHash(){super(Hash.class);}
	public static Hash create(boolean withChildren){return (new TestXmlHash()).build(withChildren);}    
    
    public Hash build(boolean withChildren)
    {
    	Hash xml = new Hash();
    	xml.setValue("myHash");
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlHash test = new TestXmlHash();
		test.saveReferenceXml();
    }
}