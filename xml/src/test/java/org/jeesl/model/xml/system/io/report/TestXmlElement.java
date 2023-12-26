package org.jeesl.model.xml.system.io.report;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.report.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlElement extends AbstractXmlReportTest<Element>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlElement.class);
	
	public TestXmlElement(){super(Element.class);}
	public static Element create(boolean withChildren){return (new TestXmlElement()).build(withChildren);}   
    
    public Element build(boolean withChildren)
    {
    	Element element = new Element();
    	element.setFile("ahtutils-basic");
    	element.setType("header");
    	return element;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlElement test = new TestXmlElement();
		test.saveReferenceXml();
    }
}