package org.jeesl.model.xml.system.util.text;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.cms.text.Example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlExample extends AbstractXmlTextTest<Example>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlExample.class);
	
	public TestXmlExample(){super(Example.class);}
	public static Example create(boolean withChildren){return (new TestXmlExample()).build(withChildren);}
    
    public Example build(boolean withChilds){return create(withChilds,"myKey","myValue");}
    public static Example create(boolean withChilds, String key, String description)
    {
    	Example xml = new Example();
    	xml.setVersion(1);
    	xml.setKey(key);
    	xml.setValue(description);
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlExample test = new TestXmlExample();
		test.saveReferenceXml();
    }
}