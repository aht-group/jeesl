package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.SubPhase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlSubPhase extends AbstractXmlStatusTest<SubPhase>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlSubPhase.class);
	
	public TestXmlSubPhase(){super(SubPhase.class);}
	public static SubPhase create(boolean withChildren){return (new TestXmlSubPhase()).build(withChildren);} 
    
    public SubPhase build(boolean withChilds)
    {
    	SubPhase xml = new SubPhase();
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
		TestXmlSubPhase test = new TestXmlSubPhase();
		test.saveReferenceXml();
    }
}