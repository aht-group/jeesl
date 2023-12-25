package org.jeesl.model.xml.dev.qa;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.module.dev.qa.PreCondition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlPreCondition extends AbstractXmlQaTest<PreCondition>
{
	final static Logger logger = LoggerFactory.getLogger(org.jeesl.model.xml.module.dev.qa.Test.class);
	
	public TestXmlPreCondition(){super(PreCondition.class);}
	public static PreCondition create(boolean withChildren){return (new TestXmlPreCondition()).build(withChildren);}   
    
    public PreCondition build(boolean withChildren)
    {
    	PreCondition xml = new PreCondition();
    	
    	xml.setValue("myPreCondition");
    	
    	return xml;
    }
    
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();	
		TestXmlPreCondition test = new TestXmlPreCondition();
		test.saveReferenceXml();
    }
}