package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlLevel extends AbstractXmlStatusTest<Level>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlLevel.class);
	
	public TestXmlLevel(){super(Level.class);}
	public static Level create(boolean withChildren){return (new TestXmlLevel()).build(withChildren);} 
    
    public Level build(boolean withChilds)
    {
    	Level xml = new Level();
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
		TestXmlLevel test = new TestXmlLevel();
		test.saveReferenceXml();
    }
}