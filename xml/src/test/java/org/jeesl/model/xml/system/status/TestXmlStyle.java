package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.Style;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlStyle extends AbstractXmlStatusTest<Style>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlStyle.class);
	
	public TestXmlStyle(){super(Style.class);}
	public static Style create(boolean withChildren){return (new TestXmlStyle()).build(withChildren);} 
    
    public Style build(boolean withChilds)
    {
    	Style xml = new Style();
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
		TestXmlStyle test = new TestXmlStyle();
		test.saveReferenceXml();
    }
}