package org.jeesl.model.xml.dev.qa;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.qa.Expected;

public class TestXmlExpected extends AbstractXmlQaTest<Expected>
{
	final static Logger logger = LoggerFactory.getLogger(net.sf.ahtutils.xml.qa.Test.class);
	
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