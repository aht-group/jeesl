package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlFunction extends AbstractXmlStatusTest<Function>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlFunction.class);
	
	public TestXmlFunction(){super(Function.class);}
	public static Function create(boolean withChildren){return (new TestXmlFunction()).build(withChildren);} 
    
    public Function build(boolean withChilds)
    {
    	Function xml = new Function();
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
    		xml.setParent(TestXmlParent.create(false));
    	}
    	
    	return xml;
    }
	
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlFunction test = new TestXmlFunction();
		test.saveReferenceXml();
    }
}