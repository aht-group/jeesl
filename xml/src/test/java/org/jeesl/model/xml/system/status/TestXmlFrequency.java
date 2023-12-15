package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.Frequency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlFrequency extends AbstractXmlStatusTest<Frequency>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlFrequency.class);
	
	public TestXmlFrequency(){super(Frequency.class);}
	public static Frequency create(boolean withChildren){return (new TestXmlFrequency()).build(withChildren);} 
    
    public Frequency build(boolean withChilds)
    {
    	Frequency xml = new Frequency();
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
		TestXmlFrequency test = new TestXmlFrequency();
		test.saveReferenceXml();
    }
}