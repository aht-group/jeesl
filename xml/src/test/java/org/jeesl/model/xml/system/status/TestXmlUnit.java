package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.Unit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlUnit extends AbstractXmlStatusTest<Unit>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlUnit.class);
	
	public TestXmlUnit(){super(Unit.class);}
	public static Unit create(boolean withChildren){return (new TestXmlUnit()).build(withChildren);}   
    
    public Unit build(boolean withChilds)
    {
    	Unit xml = new Unit();
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
		TestXmlUnit test = new TestXmlUnit();
		test.saveReferenceXml();
    }
}