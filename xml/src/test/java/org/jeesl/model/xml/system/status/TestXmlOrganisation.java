package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.Organisation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlOrganisation extends AbstractXmlStatusTest<Organisation>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlOrganisation.class);
	
	public TestXmlOrganisation(){super(Organisation.class);}
	public static Organisation create(boolean withChildren){return (new TestXmlOrganisation()).build(withChildren);} 
    
    public Organisation build(boolean withChilds)
    {
    	Organisation xml = new Organisation();
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
    		
    		xml.getFunction().add(TestXmlFunction.create(false));xml.getFunction().add(TestXmlFunction.create(false));
    	}
    	
    	return xml;
    }
    
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();	
		TestXmlOrganisation test = new TestXmlOrganisation();
		test.saveReferenceXml();
    }
}