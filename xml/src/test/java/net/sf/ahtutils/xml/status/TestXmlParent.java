package net.sf.ahtutils.xml.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.system.status.AbstractXmlStatusTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlParent extends AbstractXmlStatusTest<Parent>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlParent.class);
	
	public TestXmlParent(){super(Parent.class);}
	public static Parent create(boolean withChildren){return (new TestXmlParent()).build(withChildren);} 
    
    public Parent build(boolean withChilds)
    {
    	Parent xml = new Parent();
    	xml.setId(123);
    	xml.setCode("myCode");
    	xml.setPosition(2);
    	
    	if(withChilds)
    	{
    		
    	}
    	
    	return xml;
    }
    
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlParent test = new TestXmlParent();
		test.saveReferenceXml();
    }
}