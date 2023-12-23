package org.jeesl.model.xml.dev.qa;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.module.dev.qa.Actual;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlActual extends AbstractXmlQaTest<Actual>
{
	final static Logger logger = LoggerFactory.getLogger(org.jeesl.model.xml.module.dev.qa.Test.class);
	
	public TestXmlActual(){super(Actual.class);}
	public static Actual create(boolean withChildren){return (new TestXmlActual()).build(withChildren);}
    
    public Actual build(boolean withChilds)
    {
    	Actual xml = new Actual();
    	xml.setValue("myActual");
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlActual test = new TestXmlActual();
		test.saveReferenceXml();
    }
}