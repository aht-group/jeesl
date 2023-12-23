package org.jeesl.model.xml.dev.qa;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.module.dev.qa.Steps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlSteps extends AbstractXmlQaTest<Steps>
{
	final static Logger logger = LoggerFactory.getLogger(org.jeesl.model.xml.module.dev.qa.Test.class);
	
	public TestXmlSteps(){super(Steps.class);}
	public static Steps create(boolean withChildren){return (new TestXmlSteps()).build(withChildren);}  
    
    public Steps build(boolean withChilds)
    {
    	Steps xml = new Steps();
    	
    	xml.setValue("mySteps");
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlSteps test = new TestXmlSteps();
		test.saveReferenceXml();
    }
}