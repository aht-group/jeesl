package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.Satisfaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlSatisfaction extends AbstractXmlStatusTest<Satisfaction>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlSatisfaction.class);
	
	public TestXmlSatisfaction(){super(Satisfaction.class);}
	public static Satisfaction create(boolean withChildren){return (new TestXmlSatisfaction()).build(withChildren);} 
    
    public Satisfaction build(boolean withChilds)
    {
    	Satisfaction xml = new Satisfaction();
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
		TestXmlSatisfaction test = new TestXmlSatisfaction();
		test.saveReferenceXml();
    }
}