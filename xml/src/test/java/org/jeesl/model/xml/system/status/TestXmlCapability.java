package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.Capability;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlCapability extends AbstractXmlStatusTest<Capability>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlCapability.class);
	
	public TestXmlCapability(){super(Capability.class);}
	public static Capability create(boolean withChildren){return (new TestXmlCapability()).build(withChildren);}   
    
    public Capability build(boolean withChilds)
    {
    	Capability xml = new Capability();
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
		TestXmlCapability test = new TestXmlCapability();
		test.saveReferenceXml();
    }
}