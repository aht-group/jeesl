package org.jeesl.model.xml.dev.qa;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.module.dev.qa.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlReference extends AbstractXmlQaTest<Reference>
{
	final static Logger logger = LoggerFactory.getLogger(org.jeesl.model.xml.module.dev.qa.Test.class);
	
	public TestXmlReference(){super(Reference.class);}
	public static Reference create(boolean withChildren){return (new TestXmlReference()).build(withChildren);}   
    
    public Reference build(boolean withChildren)
    {
    	Reference xml = new Reference();
    	
    	xml.setValue("myReference");

    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlReference test = new TestXmlReference();
		test.saveReferenceXml();
    }
}