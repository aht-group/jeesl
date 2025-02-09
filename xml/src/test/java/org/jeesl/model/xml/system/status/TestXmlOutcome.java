package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.jeesl.model.xml.io.locale.status.Outcome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestXmlOutcome extends AbstractXmlStatusTest<Outcome>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlOutcome.class);
	
	public TestXmlOutcome(){super(Outcome.class);}
	public static Outcome create(boolean withChildren){return (new TestXmlOutcome()).build(withChildren);}   
   
    public Outcome build(boolean withChilds)
    {
    	Outcome xml = new Outcome();
    	xml.setId(123l);
    	xml.setCode("myCode");
    	xml.setVisible(true);
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
		TestXmlOutcome test = new TestXmlOutcome();
		test.saveReferenceXml();
    }
}