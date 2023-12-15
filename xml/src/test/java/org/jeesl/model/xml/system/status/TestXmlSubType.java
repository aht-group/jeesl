package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.SubType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlSubType extends AbstractXmlStatusTest<SubType>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlSubType.class);
	
	public TestXmlSubType(){super(SubType.class);}
	public static SubType create(boolean withChildren){return (new TestXmlSubType()).build(withChildren);} 
    
    public SubType build(boolean withChilds)
    {
    	SubType xml = new SubType();
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
		TestXmlSubType test = new TestXmlSubType();
		test.saveReferenceXml();
    }
}