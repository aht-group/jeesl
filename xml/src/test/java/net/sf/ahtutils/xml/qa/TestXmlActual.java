package net.sf.ahtutils.xml.qa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.test.UtilsXmlTestBootstrap;

public class TestXmlActual extends AbstractXmlQaTest<Actual>
{
	final static Logger logger = LoggerFactory.getLogger(net.sf.ahtutils.xml.qa.Test.class);
	
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
		UtilsXmlTestBootstrap.init();
		TestXmlActual test = new TestXmlActual();
		test.saveReferenceXml();
    }
}