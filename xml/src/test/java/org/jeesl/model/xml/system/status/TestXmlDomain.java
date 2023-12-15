package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.Domain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlDomain extends AbstractXmlStatusTest<Domain>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlDomain.class);
	
	public TestXmlDomain(){super(Domain.class);}
	public static Domain create(boolean withChildren){return (new TestXmlDomain()).build(withChildren);} 
    
    public Domain build(boolean withChilds)
    {
    	Domain xml = new Domain();
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
		TestXmlDomain test = new TestXmlDomain();
		test.saveReferenceXml();
    }
}