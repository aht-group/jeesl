package org.jeesl.model.xml.system.status;

import org.jeesl.JeeslXmlTestBootstrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.ahtutils.xml.status.Call;

public class TestXmlCall extends AbstractXmlStatusTest<Call>
{
	final static Logger logger = LoggerFactory.getLogger(TestXmlCall.class);
	
	public TestXmlCall(){super(Call.class);}
	public static Call create(boolean withChildren){return (new TestXmlCall()).build(withChildren);} 
    
    public Call build(boolean withChilds)
    {
    	Call xml = new Call();
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
		TestXmlCall test = new TestXmlCall();
		test.saveReferenceXml();
    }
}