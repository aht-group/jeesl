package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.Quarter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlQuarter extends AbstractXmlStatusTest<Quarter>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlQuarter.class);
	
	public TestXmlQuarter(){super(Quarter.class);}
	public static Quarter create(boolean withChildren){return (new TestXmlQuarter()).build(withChildren);} 
    
    public Quarter build(boolean withChilds)
    {
    	Quarter xml = new Quarter();
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
		TestXmlQuarter test = new TestXmlQuarter();
		test.saveReferenceXml();
    }
}