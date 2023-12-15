package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.Class;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlClass extends AbstractXmlStatusTest<Class>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlClass.class);
	
	public TestXmlClass(){super(Class.class);}
	public static Class create(boolean withChildren){return (new TestXmlClass()).build(withChildren);} 
    
    public Class build(boolean withChilds)
    {
    	Class xml = new Class();
    	xml.setCode("myCode");
    	xml.setVisible(true);
    	xml.setGroup("myGroup");
    	xml.setLabel("myLabel");
    	xml.setImage("test/green.png");
    	xml.setPosition(1);
    	
    	if(withChilds)
    	{
    		xml.setLangs(TestXmlLangs.create(false));
    		xml.setDescriptions(TestXmlDescriptions.create(false));
    	}
    	
    	return xml;
    }
    
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlClass test = new TestXmlClass();
		test.saveReferenceXml();
    }
}