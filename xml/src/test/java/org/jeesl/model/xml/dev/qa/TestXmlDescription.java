package org.jeesl.model.xml.dev.qa;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.module.dev.qa.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlDescription extends AbstractXmlQaTest<Description>
{
	final static Logger logger = LoggerFactory.getLogger(org.jeesl.model.xml.module.dev.qa.Test.class);
	
	public TestXmlDescription(){super(Description.class);}
	public static Description create(boolean withChildren){return (new TestXmlDescription()).build(withChildren);} 
    
    public Description build(boolean withChildren)
    {
    	Description xml = new Description();
    	
    	xml.setValue("myDescription");
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlDescription test = new TestXmlDescription();
		test.saveReferenceXml();
    }
}