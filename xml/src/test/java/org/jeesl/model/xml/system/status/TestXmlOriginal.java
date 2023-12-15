package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.Original;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlOriginal extends AbstractXmlStatusTest<Original>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlOriginal.class);
	
	public TestXmlOriginal(){super(Original.class);}
	public static Original create(boolean withChildren){return (new TestXmlOriginal()).build(withChildren);} 
    
    public Original build(boolean withChilds)
    {
    	Original xml = new Original();
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
		TestXmlOriginal test = new TestXmlOriginal();
		test.saveReferenceXml();
    }
}