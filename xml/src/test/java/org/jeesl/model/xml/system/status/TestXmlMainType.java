package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.MainType;

public class TestXmlMainType extends AbstractXmlStatusTest<MainType>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlMainType.class);
	
	public TestXmlMainType(){super(MainType.class);}
	public static MainType create(boolean withChildren){return (new TestXmlMainType()).build(withChildren);} 
    
    public MainType build(boolean withChilds)
    {
    	MainType xml = new MainType();
    	xml.setId(123);
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
		TestXmlMainType test = new TestXmlMainType();
		test.saveReferenceXml();
    }
}