package org.jeesl.model.xml.system.util;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlProperty extends AbstractXmlUtilsTest<Property>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlProperty.class);
	
	public TestXmlProperty(){super(Property.class);}
	public static Property create(boolean withChildren){return (new TestXmlProperty()).build(withChildren);}
	
    public Property build(boolean withChilds)
    {
    	Property xml = new Property();
        xml.setKey("myKey");
        xml.setValue("myValue");
        xml.setFrozen(true);
    	
    	if(withChilds)
    	{
    		
    	}
    	
    	return xml;
    }
    	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlProperty test = new TestXmlProperty();
		test.saveReferenceXml();
    }
}