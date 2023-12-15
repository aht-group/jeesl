package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.Phase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlPhase extends AbstractXmlStatusTest<Phase>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlPhase.class);
	
	public TestXmlPhase(){super(Phase.class);}
	public static Phase create(boolean withChildren){return (new TestXmlPhase()).build(withChildren);} 
    
    public Phase build(boolean withChilds)
    {
    	Phase xml = new Phase();
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
		TestXmlPhase test = new TestXmlPhase();
		test.saveReferenceXml();
    }
}