package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.Priority;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlPriority extends AbstractXmlStatusTest<Priority>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlPriority.class);
	
	public TestXmlPriority(){super(Priority.class);}
	public static Priority create(boolean withChildren){return (new TestXmlPriority()).build(withChildren);}   
    
    public Priority build(boolean withChildren)
    {
    	Priority xml = new Priority();
    	xml.setId(123l);
    	xml.setCode("myCode");
    	xml.setVisible(true);
    	xml.setGroup("myGroup");
    	xml.setLabel("myLabel");
    	xml.setImage("test/green.png");
    	xml.setPosition(1);
    	
    	if(withChildren)
    	{
    		xml.setLangs(TestXmlLangs.create(false));
    		xml.setDescriptions(TestXmlDescriptions.create(false));
    	}

    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlPriority test = new TestXmlPriority();
		test.saveReferenceXml();
    }
}