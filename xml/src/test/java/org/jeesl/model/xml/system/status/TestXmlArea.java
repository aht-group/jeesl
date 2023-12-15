package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.Area;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlArea extends AbstractXmlStatusTest<Area>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlArea.class);
	
	public TestXmlArea(){super(Area.class);}
	public static Area create(boolean withChildren){return (new TestXmlArea()).build(withChildren);}   
   
    public Area build(boolean withChilds)
    {
    	Area xml = new Area();
    	xml.setId(123l);
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
		TestXmlArea test = new TestXmlArea();
		test.saveReferenceXml();
    }
}