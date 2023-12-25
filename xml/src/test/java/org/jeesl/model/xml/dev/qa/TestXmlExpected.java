package org.jeesl.model.xml.dev.qa;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.module.dev.qa.Expected;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlExpected extends AbstractXmlQaTest<Expected>
{
	final static Logger logger = LoggerFactory.getLogger(org.jeesl.model.xml.module.dev.qa.Test.class);
	
	public TestXmlExpected(){super(Expected.class);}
	public static Expected create(boolean withChildren){return (new TestXmlExpected()).build(withChildren);} 
    
    public Expected build(boolean withChildren)
    {
    	Expected xml = new Expected();
    	
    	xml.setValue("myExpected");
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlExpected test = new TestXmlExpected();
		test.saveReferenceXml();
    }
}