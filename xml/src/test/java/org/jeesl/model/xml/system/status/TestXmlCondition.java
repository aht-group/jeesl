package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Condition;

public class TestXmlCondition extends AbstractXmlStatusTest<Condition>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlCondition.class);
	
	public TestXmlCondition(){super(Condition.class);}
	public static Condition create(boolean withChildren){return (new TestXmlCondition()).build(withChildren);} 
    
    public Condition build(boolean withChilds)
    {
    	Condition xml = new Condition();
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
    		xml.getLang().add(TestXmlLang.create(false));
    		xml.setTransistions(TestXmlTransistions.create(false));
    	}
    	
    	return xml;
    }
    
	public static void main(String[] args)
    {
		JeeslXmlTestBootstrap.init();
		TestXmlCondition test = new TestXmlCondition();
		test.saveReferenceXml();
    }
}