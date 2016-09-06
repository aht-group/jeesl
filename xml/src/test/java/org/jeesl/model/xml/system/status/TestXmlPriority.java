package org.jeesl.model.xml.system.status;

import org.jeesl.UtilsXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Priority;
import net.sf.ahtutils.xml.status.TestXmlDescriptions;
import net.sf.ahtutils.xml.status.TestXmlLangs;

public class TestXmlPriority extends AbstractXmlStatusTest<Priority>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlPriority.class);
	
	public TestXmlPriority(){super(Priority.class);}
	public static Priority create(boolean withChildren){return (new TestXmlPriority()).build(withChildren);}   
    
    public Priority build(boolean withChildren)
    {
    	Priority xml = new Priority();
    	xml.setId(123);
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
		UtilsXmlTestBootstrap.init();
		TestXmlPriority test = new TestXmlPriority();
		test.saveReferenceXml();
    }
}