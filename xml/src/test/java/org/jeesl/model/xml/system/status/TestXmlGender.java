package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.Gender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlGender extends AbstractXmlStatusTest<Gender>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlGender.class);
	
	public TestXmlGender(){super(Gender.class);}
	public static Gender create(boolean withChildren){return (new TestXmlGender()).build(withChildren);} 
    
    public Gender build(boolean withChilds)
    {
    	Gender xml = new Gender();
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
		TestXmlGender test = new TestXmlGender();
		test.saveReferenceXml();
    }
}