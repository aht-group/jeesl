package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.Legend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlLegend extends AbstractXmlStatusTest<Legend>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlLegend.class);
	
	public TestXmlLegend(){super(Legend.class);}
	public static Legend create(boolean withChildren){return (new TestXmlLegend()).build(withChildren);} 
    
    public Legend build(boolean withChilds)
    {
    	Legend xml = new Legend();
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
		TestXmlLegend test = new TestXmlLegend();
		test.saveReferenceXml();
    }
}