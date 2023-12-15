package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.Family;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlFamily extends AbstractXmlStatusTest<Family>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlFamily.class);
	
	public TestXmlFamily(){super(Family.class);}
	public static Family create(boolean withChildren){return (new TestXmlFamily()).build(withChildren);} 
    
    public Family build(boolean withChilds)
    {
    	Family xml = new Family();
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
		TestXmlFamily test = new TestXmlFamily();
		test.saveReferenceXml();
    }
}